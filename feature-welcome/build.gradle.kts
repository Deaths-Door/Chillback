import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    //applyDefaultHierarchyTemplate()

   /* androidTarget {
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

    jvm("desktop")*/

    wasmJs {
        moduleName = "welcome"
        browser {
            commonWebpackConfig {
                outputFileName = "welcome.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core-layout"))
                implementation(compose.components.resources)

                // For Current Year
                implementation(libs.kotlin.datetime)
            }
        }

        val wasmJsMain by getting {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback.welcome"

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


// falling-balls-mpp/jsApp/build.gradle.kts

// fix warning: Task ':jsApp:jsProcessResources' uses this output of task ':common:unpackSkikoWasmRuntimeJs' ...
tasks.matching { it.name == "jsProcessResources" }.configureEach {
    inputs.dir(tasks.named<org.jetbrains.compose.experimental.web.tasks.ExperimentalUnpackSkikoWasmRuntimeTask>("unpackSkikoWasmRuntimeJs").map { it.outputDir })
}

// fix warning: Task ':jsApp:jsBrowserDevelopmentRun' uses this output of task ':common:jsDevelopmentExecutableCompileSync' ...
tasks.matching { it.name == "jsBrowserDevelopmentRun" }.configureEach {
    inputs.dir(tasks.named<Copy>("jsDevelopmentExecutableCompileSync").map { it.destinationDir })
}