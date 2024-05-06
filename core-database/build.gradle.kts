plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
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
            baseName = "core-database"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                // For Local Database
                implementation(libs.sqlite.runtime)
                api(libs.sqlite.coroutines)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Local Database
                implementation(libs.sqlite.android.driver)

                // For Context
                implementation(libs.androidx.startup.runtime)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Local Database
                implementation(libs.sqlite.driver)
            }
        }

        val nativeMain by getting {
            dependsOn(commonMain)
            dependencies {
                // For Local Database
                implementation(libs.sqlite.native.driver)
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.core.database"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sqldelight {
    databases {
        create("AppLocalDatabase") {
            packageName.set("com.deathsdoor.core.database")
        }
    }
}