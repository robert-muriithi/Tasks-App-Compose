plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-parcelize")
}

apply {
    from("$rootDir/compose-dependencies.gradle")
}
apply {
    from("$rootDir/core-dependencies.gradle")
}
apply {
    from("$rootDir/testing-dependencies.gradle")
}
apply {
    from("$rootDir/firebase.gradle")
}
apply {
    from("$rootDir/versioning.gradle")
}

android {
    namespace = "dev.robert.compose_todo"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.robert.compose_todo"
        minSdk = 24
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
//            applicationIdSuffix = ".debug"
            isDebuggable = true
            multiDexEnabled = false
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.kotlinx.json)
    implementation(project(path = ":core:database"))
    implementation(project(path = ":core:design-system"))
    implementation(project(path = ":core:datastore"))
    implementation(project(path = ":feature:onboarding"))
    implementation(project(path = ":feature:settings"))
    implementation(project(path = ":feature:tasks"))
    implementation(project(path = ":feature:auth"))
    implementation(project(path = ":feature:profile"))
    implementation(project(path = ":core:shared"))
    implementation(project(path = ":core:navigation"))
}
