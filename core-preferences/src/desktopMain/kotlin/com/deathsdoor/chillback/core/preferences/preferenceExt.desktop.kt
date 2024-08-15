package com.deathsdoor.chillback.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

internal actual fun createDataStore(): DataStore<Preferences> = createDataStore {
    val os = System.getProperty("os.name").lowercase()

    val directory = when {
        os.contains("windows") -> platformDirectoryForPreferences("USERNAME","AppData/Local")
        os.contains("linux") -> platformDirectoryForPreferences("HOME",".config")
        os.contains("mac") -> platformDirectoryForPreferences("HOME","Library/Preferences")
        else -> error("Unsupported operating system: $os")
    }

    val applicationDirectory = "$directory/Chillback"

    val file = File(applicationDirectory)
    if(!file.exists()) file.mkdirs()

    // Similar logic for android
    return@createDataStore file.resolve(dataStoreFileName).absolutePath
}

private fun platformDirectoryForPreferences(environmentVariable : String,childDirectory: String): String {
    val env = System.getenv(environmentVariable)
    return "$env/$childDirectory"
}