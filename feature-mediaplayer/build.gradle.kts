import dev.icerock.gradle.MRVisibility

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id(libs.plugins.resources.get().pluginId)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop")

    if(System.getProperty("os.name") == "Mac OS X")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "feature-mediaplayer"
            isStatic = true
        }

        tasks.named("${it.name}ProcessResources") {
            dependsOn("generateMRcommonMain")
            dependsOn("generateMR${it.name}Main")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core-layout"))
                implementation(project(":core-preferences"))

                api(libs.deathsdooor.astroplayer.core)
                api(libs.deathsdooor.astroplayer.ui)

                implementation(libs.coil.compose.core)
                implementation(libs.coil.compose)
                implementation(libs.coil.mp)
                implementation(libs.coil.network.ktor)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.io.core)

                implementation(libs.ui.tiles)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val desktopMain by getting {
            dependsOn(commonMain)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback.feature.mediaplayer"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    // https://youtrack.jetbrains.com/issue/KT-42388/CompilationException-Back-end-JVM-Internal-error-Couldnt-inline-method-call-get-current-into-androidx.compose.runtime.Composable
    composeOptions.kotlinCompilerVersion = libs.versions.kotlin.get()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


multiplatformResources {
    multiplatformResourcesPackage = "com.deathsdoor.chillback.feature.mediaplayer.resources"
    multiplatformResourcesVisibility = MRVisibility.Internal
    multiplatformResourcesClassName = "Res"
}