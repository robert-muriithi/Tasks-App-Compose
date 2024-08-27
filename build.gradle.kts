
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.google.services) apply false
//    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.module.graph)
//    id("dev.iurysouza.modulegraph") version "0.10.0"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        debug.set(true)
        verbose.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }

    dependencies {
        //noinspection UseTomlInstead
        "ktlint"("io.nlopez.compose.rules:ktlint:0.4.10")
    }
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
}
