plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.mokoResources) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.sqlDelight) apply false
    alias(libs.plugins.jsonSerialization) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.resources.generator)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.perf.gradle)
        classpath(libs.sqlite.gradle)
    }
}