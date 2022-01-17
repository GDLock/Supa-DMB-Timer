object Dependencies {

    object Compose {
        const val version = "1.1.0-rc01"
        const val compilerV = "1.1.0-rc02"

        const val ui = "androidx.compose.ui:ui:$version"
        const val ui_tooling = "androidx.compose.ui:ui-tooling:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val compiler = "androidx.compose.compiler:compiler:$compilerV"
        const val animation = "androidx.compose.animation:animation:$version"
        const val material_icon = "androidx.compose.material:material-icons-extended:$version"
        const val material = "androidx.compose.material:material:$version"
    }

    object DataStore {
        private const val version = "1.0.0"

        const val dataStore = "androidx.datastore:datastore:1.0.0"
        const val preferences = "androidx.datastore:datastore-preferences:1.0.0"
    }

    object Test {
        const val jUnit = "junit:junit:4.13.2"
    }

    object Accompanist {
        private const val version = "0.22.0-rc"

        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val system_ui_controller = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val animated_navigation = "com.google.accompanist:accompanist-navigation-animation:$version"
    }

    const val navigation = "androidx.navigation:navigation-compose:2.4.0-rc01"

    object Core {
        const val core = "androidx.core:core-ktx:1.7.0"
        const val app_compat = "androidx.appcompat:appcompat:1.4.0"
        const val activity_compose = "androidx.activity:activity-compose:1.4.0"
        const val viewmodel_compose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"
        const val splash_screen = "androidx.core:core-splashscreen:1.0.0-beta01"
        const val desugaring = "com.android.tools:desugar_jdk_libs:1.1.5"
    }

    object Coroutines {
        private const val version = "1.6.0"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Hilt {
        private const val version = "2.38.1"

        const val hilt = "com.google.dagger:hilt-android:$version"
        const val android_compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val compiler = "androidx.hilt:hilt-compiler:1.0.0"
        const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0-rc01"
    }

    object Plugins {
        private const val kotlin = "1.6.10"

        const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.0-rc01"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:2.38.1"
    }
}