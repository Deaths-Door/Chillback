plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
}

object Metadata {
    const val javaVersion = "11"
    val asJavaVersionEnum = JavaVersion.values().find { it.name.endsWith(javaVersion) }
    const val minSDK = 26
    const val maxSDK = 33

    const val namespace = "com.deathsdoor.chillback"
    const val versionName = "0.0.1"
}

android {
    namespace = Metadata.namespace
    compileSdk = Metadata.maxSDK + 1 // To go from 33 to 34

    defaultConfig {
        applicationId = Metadata.namespace
        minSdk = Metadata.minSDK
        targetSdk = Metadata.maxSDK

        versionName = Metadata.versionName
    }

    Metadata.asJavaVersionEnum.also {
        compileOptions.sourceCompatibility = it
        compileOptions.targetCompatibility = it
    }

    kotlinOptions.jvmTarget = Metadata.javaVersion

    buildTypes.getByName("release"){
        isMinifyEnabled = true
        isShrinkResources = true

        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }

    packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation("androidx.activity:activity-compose:1.8.2")
}