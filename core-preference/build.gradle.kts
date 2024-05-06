plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinxAtomicFu)
}

kotlin {
    applyDefaultHierarchyTemplate()

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
            baseName = "core-preference"
            isStatic = true
        }
    }

    jvm("desktop")

    // TODO : Wait for firebase-support for wasmJs
    /*wasmJs {
        binaries.executable()
    }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Preferences
                api(libs.multiplatform.settings.coroutines)
                api(libs.multiplatform.settings.no.arg)

                api(project(":core-database"))

                // Firebase
                implementation(libs.firebase.auth)
                implementation(libs.firebase.database)

                implementation(libs.atomicfu)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Native Android Firebase
                implementation(libs.firebase.android.crashlytics)
                implementation(libs.firebase.android.perf)
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.core.preference"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
        // For Firebase
        implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    }
}
