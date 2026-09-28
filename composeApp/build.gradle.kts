import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
// The dev server (wasmJsBrowserDevelopmentRun) serves index.html untouched; the placeholder comment is harmless.
val webFirstFrameResources =
    listOf(
        Regex("font/.*"),
        Regex("drawable/(home|ic)_.*"),
        Regex("values/strings(_home)?\\.commonMain\\.cvr"),
    )
val webPreloadPlaceholder = Regex(" *<!-- web-preloads:.*-->")

tasks.named("wasmJsBrowserDistribution") {
    val distDir = layout.buildDirectory.dir("dist/wasmJs/productionExecutable")
    val patterns = webFirstFrameResources
    val placeholder = webPreloadPlaceholder
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
        index.writeText(html.replace(placeholder, tags.joinToString("\n") { "    $it" }))
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
