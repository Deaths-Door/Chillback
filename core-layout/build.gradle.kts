plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)

    id(libs.plugins.resources.get().pluginId)
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

        // Workaround for missing implicit dependencies
        tasks.named("${it.name}ProcessResources") {
            dependsOn("generateMRcommonMain")
            dependsOn("generateMR${it.name}Main")
        }
    }

    jvm("desktop")

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

                api(libs.resources)
                api(libs.resources.compose)

                api(libs.navigation.compose)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                api(libs.compose.ui.tooling.preview)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback.core.layout"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    // https://youtrack.jetbrains.com/issue/KT-42388/CompilationException-Back-end-JVM-Internal-error-Couldnt-inline-method-call-get-current-into-androidx.compose.runtime.Composable
    composeOptions.kotlinCompilerVersion = libs.versions.kotlin.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.deathsdoor.chillback.core.resources"
    multiplatformResourcesClassName = "Res" // optional, default MR
}
