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
    implementation("androidx.core:core-ktx:1.12.0")

    // Compose
    listOf(compose.ui,compose.foundation,compose.material3,compose.runtime).forEach {
        implementation(it)
    }

    // For Navigation + ComponentActivity
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // For Settings + Preference Saving
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}