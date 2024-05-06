plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.sqlDelight) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kotlinxAtomicFu) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.google.android.services)
        classpath(libs.firebase.android.crashlytics.gradle)
        classpath(libs.firebase.android.perf.gradle)
        classpath(libs.sqlite.gradle)
        classpath(libs.atomicfu.gradle.plugin)
    }
}

apply(plugin = "kotlinx-atomicfu")