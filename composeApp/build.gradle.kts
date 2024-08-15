import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorPlugin
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Style
import java.time.Year

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.firebasePerformance)
    alias(libs.plugins.googleServices)
}

kotlin {
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
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Chillback"
            isStatic = true
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.components.uiToolingPreview)

                implementation(project(":core-layout"))
                implementation(project(":core-preferences"))

                implementation(project(":feature-welcome"))
                implementation(project(":feature-mediaplayer"))

                implementation(libs.androidx.lifecycle.viewmodel)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.deathsdoor.chillback"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    //composeOptions.kotlinCompilerVersion = libs.versions.kotlin.get()

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation(libs.androidx.activity.compose) {
            // To Fix - Duplicate class androidx.lifecycle.ViewModelKt found in modules (androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1) and (androidx.lifecycle:lifecycle-viewmodel-android:2.8.0-alpha03)
           // exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
        }

        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "com.deathsdoor.chillback"
            packageVersion = "1.0.0"

            description = "Chillback is an open-source music player built for those who prioritize a relaxed listening experience. Unlike resource-hungry options, Chillback is designed for smooth and efficient performance, making it ideal for users who value battery life or lower-powered devices. Packed with a modern, customizable UI, Chillback empowers you to tailor the player to your preferences, offering a compelling alternative to Spotify."
            vendor = "Aarav Shah"

            // https://stackoverflow.com/a/33700141
            copyright = "© ${Year.now().value} $vendor. All rights reserved."
            licenseFile.set(project.file("LICENSE"))

            linux {
                debMaintainer = "aaravaditya51@gmail.com"
            }

            windows {
                dirChooser = true
                perUserInstall = true
            }
        }
    }
}

// ----------------- Dependency Graph ---------------------
plugins.apply(DependencyGraphGeneratorPlugin::class.java)
configure<DependencyGraphGeneratorExtension> {
    generators.create("ChillbackDependencyGraph") {
        children = { true }
        dependencyNode = { node, dependency -> node.add(Style.FILLED, Color.rgb("#B02F00")) } // Give them some color.
    }
}