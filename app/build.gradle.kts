plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")

    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")

    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    ndkPath = System.getenv("ANDROID_NDK_HOME")
    namespace = Metadata.namespace
    compileSdk = Metadata.maxSDK + 1 // To go from 33 to 34

    defaultConfig {
        minSdk = Metadata.minSDK
        applicationId = namespace
        versionName = "0.0.1"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    Metadata.asJavaVersionEnum.also {
        compileOptions.sourceCompatibility = it
        compileOptions.targetCompatibility = it
    }
    
    kotlinOptions.jvmTarget = Metadata.asJavaVersionEnum.toString()

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.2"

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

private object Metadata {
    const val namespace = "com.deathsdoor.chillback"
    const val javaVersion = "17"
    val asJavaVersionEnum = JavaVersion.values().find { it.name.endsWith(javaVersion) }!!
    const val minSDK = 26
    const val maxSDK = 33
}

dependencies {
    // Using Compose
    implementation(compose.ui)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation("androidx.activity:activity-compose:1.8.2")

    // For Preview
    debugImplementation(compose.uiTooling)
    implementation(compose.preview)

    // For Adaptive UI
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha06")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-perf")

    // Google Play services library
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // For Rust
    implementation("net.java.dev.jna:jna:5.9.0@aar")

    // For Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // For Preference Saving
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Local Database
    with("2.6.1"){
        implementation("androidx.room:room-runtime:$this")
        implementation("androidx.room:room-ktx:$this")
        annotationProcessor("androidx.room:room-compiler:$this")
        ksp("androidx.room:room-compiler:$this")
    }

    // For Mediaplayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-session:1.2.0")

    // For Permissions
    implementation( "com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    // For Music Metadata
    implementation("net.jthink:jaudiotagger:3.0.1")

    // For WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // For Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // For Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // In-Memory Caching
    implementation("io.github.reactivecircus.cache4k:cache4k:0.12.0")

    // For Custom Snackbars
    implementation("io.github.rizmaulana:compose-stacked-snackbar:1.0.4")

    // For Multiple Inputs
    implementation("io.github.dokar3:chiptextfield:0.7.0-alpha01")

    // For Date Input
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // For AudioWaves
    implementation("com.github.lincollincol:compose-audiowaveform:1.1.1")
    implementation("com.github.lincollincol:amplituda:2.2.2")

    // For Loading Indicator
    implementation("com.github.MahboubehSeyedpour:jetpack-loading:1.1.0")

    // For Dropdowns
    implementation("io.github.androidpoet:dropdown:1.1.2")

    // For MultiSelect
    implementation("com.dragselectcompose:grid:2.2.6")

    // For Settings
    implementation("com.github.alorma.compose-settings:ui-tiles:2.1.0")
}

// Due to [this](https://github.com/rizmaulana/compose-stacked-snackbar?tab=readme-ov-file)
subprojects {
    project.configurations.configureEach {
        resolutionStrategy {
            force("androidx.emoji2:emoji2-views-helper:1.3.0")
            force("androidx.emoji2:emoji2:1.3.0")
        }
    }
}