plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")

    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
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
    
    kotlinOptions.jvmTarget = compileOptions.targetCompatibility.toString()

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
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose
    listOf(compose.ui,compose.foundation,compose.material3,compose.runtime).forEach {
        implementation(it)
    }

    // For Preference Saving
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // For Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // For Mediaplayer
    listOf("exoplayer","session").forEach {
        implementation("androidx.media3:media3-$it:1.2.0")
    }

    // For Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Local Database
    "2.6.1".run {
        implementation("androidx.room:room-runtime:$this")
        implementation("androidx.room:room-ktx:$this")
        annotationProcessor("androidx.room:room-compiler:$this")
        ksp("androidx.room:room-compiler:$this")
    }

    // For Music Metadata
    implementation("net.jthink:jaudiotagger:3.0.1")

    // In-Memory Caching
    implementation("io.github.reactivecircus.cache4k:cache4k:0.12.0")

    // For Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // For Permissions
    implementation( "com.google.accompanist:accompanist-permissions:0.35.0-alpha")
}