plugins {
    kotlin("android") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("com.google.gms.google-services") apply false
    id("com.google.devtools.ksp") apply false
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.mozilla.rust-android-gradle:plugin:0.9.3")
        classpath("com.google.gms:google-services:4.4.1")
    }
}