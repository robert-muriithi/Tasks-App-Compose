plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

apply {
    from("$rootDir/compose-dependencies.gradle")
}
apply {
    from("$rootDir/testing-dependencies.gradle")
}

android {
    namespace = "dev.robert.navigation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlinx.json)

    implementation(project(path = ":feature:auth"))
    implementation(project(path = ":feature:tasks"))
    implementation(project(path = ":feature:settings"))
    implementation(project(path = ":feature:onboarding"))
}
