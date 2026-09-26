plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktlint) apply false
}

val ktlintVersion: String = libs.versions.ktlint.get()

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(ktlintVersion)
        // Compose resources generate `Res` & co. into build/, which is not ours to lint.
        filter {
            exclude { it.file.path.contains("${File.separator}build${File.separator}") }
        }
    }
}
