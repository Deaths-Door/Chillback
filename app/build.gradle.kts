plugins {
    kotlin("android")
    id("com.android.application")

    id("org.jetbrains.compose")

    id("com.google.gms.google-services")

    kotlin("plugin.serialization")
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
    // Defaultly Required
    implementation("androidx.core:core-ktx:1.12.0")

    // Compose
    listOf(compose.ui,compose.foundation,compose.material3,compose.runtime).forEach {
        implementation(it)
    }

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    listOf("auth","database").forEach {
        implementation("com.google.firebase:firebase-$it-ktx")
    }

    // For Mediaplayer
    listOf("exoplayer","session").forEach {
        implementation("androidx.media3:media3-$it:1.2.0")
    }

    // For Navigation + ComponentActivity
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // For Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // For Preference Saving
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // For Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // For Setting Screens
    implementation ("com.github.alorma:compose-settings-ui-m3:1.0.3")

    // For Coordinator Layout
    implementation("me.onebone:toolbar-compose:2.3.5")

    // For Rating
    implementation("com.google.android.play:review-ktx:2.0.1")

    // For Time Input
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // For Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}