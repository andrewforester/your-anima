import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.security.MessageDigest

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // JVM target is used only for fast, emulator-free UI tests (`./gradlew :composeApp:jvmTest`).
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    // AGP 9: the Android *application* lives in :androidApp, this module is an Android library.
    android {
        namespace = "app.youranima.composeapp"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            // Required by the UI layer (docs/COORDINATION.md)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.navigation.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.uiTest)
        }
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.resources {
    packageOfResClass = "app.youranima.resources"
}

// Web startup: the production index.html starts downloading everything the first frame needs right after it is
// parsed, in parallel, instead of in three serial waves (JS → wasm → resources):
// - the two wasm files (content-hashed by webpack, hence generated here after the bundle is assembled) and
//   composeApp.js get <link rel="preload"> tags; composeApp.js consumes them within a second;
// - the first-frame composeResources are fetched by a small inline script into `window.webPreloads` (path → promise
//   of the bytes), which Main.kt reads into memory before the first composition. Not <link rel="preload">: the app
//   can only consume them once the composeApp wasm has loaded, which on a slow network is well past Chrome's
//   3-second "preloaded but not used" window.
// First-frame resources are derived from file names, not listed one by one: all fonts, the home screen's `home_*` and
// shared `ic_*` drawables, and the base + home string tables. Keep large files that the first screen doesn't show out
// of these patterns: everything matched is downloaded before the loader hides.
// The same post-step fills in the service worker (resources/sw.js, #71): its precache list is the first-frame set
// above plus the top-level app files (index.html, composeApp.js, wasm, styles, icons), and its build id is a hash of
// the whole bundle, so every deploy ships a byte-different sw.js. index.html registers it once the loader is hidden.
// The dev server (wasmJsBrowserDevelopmentRun) serves index.html untouched; the placeholder comment is harmless, and
// without the registration script the unfilled sw.js is never used.
val webFirstFrameResources =
    listOf(
        Regex("font/.*"),
        Regex("drawable/(home|ic)_.*"),
        Regex("values/strings(_home)?\\.commonMain\\.cvr"),
    )
val webPreloadPlaceholder = Regex(" *<!-- web-preloads:.*-->")

// Registers sw.js once the first frame is shown, so the install (~12 MB into Cache Storage) never competes with
// startup. A waiting newer build is activated at the very start of the next load: the page then reloads once into it
// (`controllerchange`), so no page runs on files of two builds. Only acts on this app's own registration, not on one
// of an enclosing scope (prod around a branch preview).
val serviceWorkerRegistration =
    """
    <script>if ("serviceWorker" in navigator) {
      const sw = navigator.serviceWorker, script = new URL("sw.js", location.href).href;
      let reloading = false;
      sw.addEventListener("controllerchange", () => {
        if (!reloading && sw.controller?.scriptURL === script) { reloading = true; location.reload(); }
      });
      sw.getRegistration().then((r) => {
        if (r?.waiting && sw.controller?.scriptURL === script) r.waiting.postMessage("skipWaiting");
      });
      addEventListener("DOMContentLoaded", () => {
        const loader = document.getElementById("loader");
        new MutationObserver((_, observer) => {
          if (!loader.classList.contains("hidden")) return;
          observer.disconnect();
          sw.register("sw.js", { updateViaCache: "none" });
        }).observe(loader, { attributes: true });
      });
    }</script>
    """.trimIndent().lines().joinToString("\n    ")

tasks.named("wasmJsBrowserDistribution") {
    val distDir = layout.buildDirectory.dir("dist/wasmJs/productionExecutable")
    val patterns = webFirstFrameResources
    val placeholder = webPreloadPlaceholder
    val serviceWorkerRegistration = serviceWorkerRegistration
    doLast {
        val dist = distDir.get().asFile
        val resourcesDir = dist.resolve("composeResources/app.youranima.resources")
        val wasm = dist.listFiles { f -> f.extension == "wasm" }!!.sortedByDescending { it.length() }.map { it.name }
        val resources =
            resourcesDir
                .walk()
                .filter { it.isFile }
                .map { it.relativeTo(resourcesDir).invariantSeparatorsPath }
                .filter { path -> patterns.any { it.matches(path) } }
                .sorted()
                .map { "composeResources/app.youranima.resources/$it" }
                .toList()
        check(wasm.size == 2 && resources.isNotEmpty()) { "web preloads: unexpected bundle layout in $dist" }
        val resourceList = resources.joinToString(",") { "\"$it\"" }
        val tags =
            wasm.map { """<link rel="preload" href="$it" as="fetch" type="application/wasm" crossorigin>""" } +
                """<link rel="preload" href="composeApp.js" as="script">""" +
                "<script>window.webPreloads = Object.fromEntries([$resourceList].map((p) => " +
                "[p, fetch(p).then((r) => r.ok ? r.arrayBuffer() : Promise.reject(r.status))]));</script>"
        val index = dist.resolve("index.html")
        val html = index.readText()
        check(placeholder.containsMatchIn(html)) { "web preloads: placeholder comment missing in index.html" }
        index.writeText(html.replace(placeholder, (tags + serviceWorkerRegistration).joinToString("\n") { "    $it" }))

        val appFiles =
            dist
                .listFiles { f -> f.isFile && f.name != "sw.js" && f.extension != "map" && !f.name.endsWith(".txt") }!!
                .map { it.name }
                .sorted()
        val digest = MessageDigest.getInstance("SHA-256")
        dist
            .walk()
            .filter { it.isFile && it.name != "sw.js" }
            .sortedBy { it.relativeTo(dist).invariantSeparatorsPath }
            .forEach {
                digest.update(it.relativeTo(dist).invariantSeparatorsPath.toByteArray())
                digest.update(it.readBytes())
            }
        val buildId = digest.digest().joinToString("") { "%02x".format(it) }.take(16)
        val sw = dist.resolve("sw.js")
        val swSource = sw.readText()
        check("__BUILD_ID__" in swSource && "['__PRECACHE__']" in swSource) { "sw.js: placeholders missing" }
        sw.writeText(
            swSource
                .replace("__BUILD_ID__", buildId)
                .replace("['__PRECACHE__']", (appFiles + resources).joinToString(", ", "[", "]") { "'$it'" }),
        )
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
