plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
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
            baseName = "core-layout"
            isStatic = true
        }
    }

    jvm("desktop")

    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "core_layout.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // For Compsoe
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.ui)
                api(compose.components.uiToolingPreview)

                // For WindowSize Class till its KMP Ready
                api(libs.material3.window.size.multiplatform)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.compose.ui.tooling.preview)
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.core.layout"
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
        // For Compose Preview
        debugApi(libs.compose.ui.tooling)
    }
}
