import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.mokoResources)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.jsonSerialization)
}

private object ModuleMetadata {
    val applicationName = "Chillback"
    val namespace = "com.deathsdoor.chillback"
    val applicationVersion = "1.0.0"
    val applicationVersionAsInteger =
        applicationVersion.split(".").fold(0) { acc, num -> acc * 100 + num.toInt() }

    val javaVersion = "17"
    val asJavaVersionEnum = JavaVersion.values().find { it.name.endsWith(javaVersion) }!!

}

kotlin {
    applyDefaultHierarchyTemplate()
    /*@OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = ModuleMetadata.applicationName.lowercase()
        browser {
            commonWebpackConfig {
                outputFileName = "${ModuleMetadata.applicationName.lowercase()}.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }*/

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = ModuleMetadata.javaVersion
            }
        }
    }

    jvm("desktop")

    // TODO : Enable this in the future rn viewmodel-compose doesnt have ios support
    /*listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = ModuleMetadata.applicationName
            isStatic = true
        }
    }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                // For Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)

                // For Resources
                implementation(libs.resources)
                implementation(libs.resources.compose)

                // For WindowSize Class till its KMP Ready
                implementation(libs.material3.window.size.multiplatform)

                // For Android Viewmodel
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.viewmodel.compose)

                // For Navigation
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenModel)
                implementation(libs.voyager.tabNavigator)
                implementation(libs.voyager.transitions)

                // For Preference Saving
                implementation(libs.androidx.datastore.preferences)

                // Firebase
                implementation(libs.firebase.auth)
                implementation(libs.firebase.database)

                // For getting data and cache directories
                implementation(libs.paths)

                // For Local Database
                implementation(libs.sqlite.runtime)
                implementation(libs.sqlite.coroutines)

                // For Serialisation
                implementation(libs.kotlinx.serialization.json)

                // For Uri
                implementation(libs.uri.kmp)

                // For UUID
                implementation(libs.uuid)

                // For Music Metadata
                implementation(libs.jaudiotagger)

                // In-Memory Caching
                implementation(libs.cache4k)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Compose
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)

                // For Native Android Firebase
                implementation(libs.firebase.crashlytics)
                implementation(libs.firebase.perf)

                // For Mediaplayer
                implementation(libs.androidx.media3.exoplayer)
                implementation(libs.androidx.media3.session)

                // For Getting Context Anywhere for KMP
                implementation(libs.androidx.startup.runtime)

                // For Local Database
                implementation(libs.android.driver)

                // For Background Tasks
                implementation(libs.androidx.work.runtime.ktx)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Compose
                implementation(compose.desktop.currentOs)

                // For MediaPlayer
                implementation(libs.vlcj)

                // For Local Database
                implementation(libs.sqlite.driver)
            }
        }

        // TODO : Enabled WasmJs Support Later
        /*val wasmJsMain by getting {
            dependsOn(commonMain)
        }*/
    }
}

android {
    namespace = ModuleMetadata.namespace
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = ModuleMetadata.namespace
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = ModuleMetadata.applicationVersionAsInteger
        versionName = ModuleMetadata.applicationVersion
    }

    ModuleMetadata.asJavaVersionEnum.also {
        compileOptions.sourceCompatibility = it
        compileOptions.targetCompatibility = it
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    dependencies {
        // For Firebase
        implementation(platform("com.google.firebase:firebase-bom:32.7.4"))

        // For Compose Preview
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = ModuleMetadata.namespace
            packageVersion = ModuleMetadata.applicationVersion
        }
    }
}

/*
compose.experimental {
    web.application {}
}*/

multiplatformResources {
    multiplatformResourcesPackage = "${ModuleMetadata.namespace}.resources"
    multiplatformResourcesClassName = "Resources"
}

sqldelight {
    linkSqlite.set(true)

    databases {
        create("ApplicationLocalDatabase") {
            verifyMigrations.set(true)
            verifyMigrations.set(true)
            verifyDefinitions.set(true)
            packageName.set("${ModuleMetadata.namespace}.data.database")
        }
    }
}

// Dummy task to fix this
// Cannot locate tasks that match ':composeApp:testClasses' as task 'testClasses' not found in project ':composeApp'.
task("testClasses")
