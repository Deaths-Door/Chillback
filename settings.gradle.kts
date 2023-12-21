pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("android") version(extra["kotlin.version"] as String)
        id("com.android.application") version(extra["agp.version"] as String)
        id("com.android.library") version(extra["agp.version"] as String)

        id("org.jetbrains.compose") version(extra["compose.version"] as String)

        id("com.google.gms.google-services") version(extra["google.services.version"] as String)
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Chillback"
include(":app")
