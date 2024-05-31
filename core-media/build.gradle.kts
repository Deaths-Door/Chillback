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

    /*if(System.getProperty("os.name") == "Mac OS X")
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "core-media"
                isStatic = true
            }

            tasks.named("${it.name}ProcessResources") {
                dependsOn("generateMRcommonMain")
                dependsOn("generateMR${it.name}Main")
            }
        }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core-layout"))

                api(libs.deathsdooor.astroplayer.core)
                api(libs.deathsdooor.astroplayer.ui)

                implementation(libs.reorderable)

                implementation(libs.kotlinx.io.core)

                api(libs.coil.compose.core)
                api(libs.coil.compose)

                implementation(libs.coil.mp)
                implementation(libs.coil.network.ktor)

                implementation(libs.cache4k)
                implementation(libs.jaudiotagger)
                implementation(libs.kotlinx.datetime)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                // Audio Waves
                implementation(libs.compose.audiowaveform)
                implementation(libs.amplituda)

                // ffmpeg
                implementation(libs.ffmpeg.kit.audio)

                // requesting ringtone setting permission
                implementation(libs.accompanist.permissions)
            }
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
    namespace = "com.deathsdoor.chillback.core.media"
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
    multiplatformResourcesPackage = "com.deathsdoor.chillback.core.media.resources"
    multiplatformResourcesVisibility = MRVisibility.Public
    multiplatformResourcesClassName = "Res"
}