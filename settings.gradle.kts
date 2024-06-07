@file:Suppress("UnstableApiUsage")

rootProject.name = "Chillback"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        // https://github.com/GitLiveApp/firebase-kotlin-sdk/issues/503
        google()

        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal {
            metadataSources {
                mavenPom()
                artifact()
                gradleMetadata()
            }
        }

        maven("https://jitpack.io")
    }
}

include(":composeApp")
include(":core-layout")
include(":core-preferences")
include(":feature-welcome")
include(":feature-mediaplayer")
include(":core-media")
