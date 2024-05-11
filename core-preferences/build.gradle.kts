plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
     alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-prefences"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.multiplatform.settings.no.arg)
                api(libs.multiplatform.settings.coroutines)

                api(libs.firebase.auth)

                api(libs.kotlinx.coroutines.core)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Native Android Firebase
                api("com.google.firebase:firebase-crashlytics")
                api("com.google.firebase:firebase-analytics")
                api("com.google.firebase:firebase-perf")
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback.core.prefences"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        // For Firebase
        implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    }
}
