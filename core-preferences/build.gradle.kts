plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.googleServices)
    // TODO: enable
    //alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    applyDefaultHierarchyTemplate()

    // To fix Cannot inline bytecode built with JVM target 17 into bytecode that is being built with JVM target 1.8. Please specify proper '-jvm-target' option
    java.toolchain.languageVersion = JavaLanguageVersion.of(17)

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    if(System.getProperty("os.name") == "Mac OS X") listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-preferences"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)

                api(libs.androidx.datastore.preferences)
                // Previous Versions
                //api(libs.multiplatform.settings.no.arg)
                //api(libs.multiplatform.settings.coroutines)

                api(libs.firebase.auth)

                // Used for preferences
                api(libs.firebase.database)

                // Used for database
                api(libs.firebase.firestore)

                // https://developer.android.com/kotlin/multiplatform/room
             //   api(libs.androidx.room.runtime)
              //  api(libs.androidx.room.paging)

                //implementation(libs.androidx.sqlite.bundled)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                // For Native Android Firebase
                implementation("com.google.firebase:firebase-perf")
                implementation("com.google.firebase:firebase-crashlytics")
                implementation("com.google.firebase:firebase-analytics")

                // https://github.com/search?q=repo%3AGitLiveApp%2Ffirebase-kotlin-sdk%20bom&type=code
                implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.0.0"))

                implementation(libs.androidx.startup.runtime)
            }
        }

        val desktopMain by getting {
            dependencies {
                // https://github.com/GitLiveApp/firebase-java-sdk/issues/16
                implementation("dev.gitlive:firebase-java-sdk:0.4.3")
            }
        }
    }
}

android {
    namespace = "com.deathsdoor.chillback.core.prefences"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

/*
room {
    schemaDirectory("$projectDir/schemas")
}

fun DependencyHandlerScope.ksp(dependencyNotation : Any) {
    add("kspCommonMainMetadata", dependencyNotation) // Run KSP on [commonMain] code
    add("kspAndroid",dependencyNotation)
}

dependencies {
    ksp(libs.androidx.room.compiler)
}*/