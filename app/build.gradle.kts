plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.superdmbtimer"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerV
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Jetpack Compose
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.ui_tooling)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.compiler)
    implementation(Dependencies.Compose.animation)
    implementation(Dependencies.Compose.material_icon)
    implementation(Dependencies.Compose.material)

    // Core
    implementation(Dependencies.Core.core)
    implementation(Dependencies.Core.app_compat)
    implementation(Dependencies.Core.activity_compose)
    implementation(Dependencies.Core.viewmodel_compose)
    implementation(Dependencies.Core.splash_screen)
    coreLibraryDesugaring(Dependencies.Core.desugaring)

    // Navigation
    implementation(Dependencies.navigation)

    //DataStore
    implementation(Dependencies.DataStore.dataStore)
    implementation(Dependencies.DataStore.preferences)

    // Accompanist
    implementation(Dependencies.Accompanist.pager)
    implementation(Dependencies.Accompanist.system_ui_controller)
    implementation(Dependencies.Accompanist.animated_navigation)

    // Coroutines
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)

    // Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.android_compiler)
    kapt(Dependencies.Hilt.compiler)
    implementation(Dependencies.Hilt.viewmodel)
    implementation(Dependencies.Hilt.navigation)

    testImplementation(Dependencies.Test.jUnit)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}