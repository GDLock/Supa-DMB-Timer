buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Plugins.androidGradlePlugin)
        classpath(Dependencies.Plugins.kotlinGradlePlugin)
        classpath(Dependencies.Plugins.hiltGradlePlugin)
    }
}