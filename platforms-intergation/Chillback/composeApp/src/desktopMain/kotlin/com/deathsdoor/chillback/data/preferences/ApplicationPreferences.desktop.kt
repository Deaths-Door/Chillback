@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.preferences

import androidx.annotation.GuardedBy
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import me.sujanpoudel.mputils.paths.appDataDirectory
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.name

/// Source Code copied from androidx.datastore.preferences.preferencesDataStore
actual class ApplicationPreferences {
    private val lock = Any()

    @GuardedBy("lock")
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null

    actual val datastore: DataStore<Preferences>
        get() = INSTANCE ?: synchronized(lock) {
            if (INSTANCE == null) {

                INSTANCE = provideDatastorePrerequisites{ corruptionHandler, scope ->
                    PreferenceDataStoreFactory.create(
                        corruptionHandler = corruptionHandler,
                        scope = scope,
                        produceFile = {
                            val dataDirectory = appDataDirectory("com.deathsdoor.chillback")
                            val fileName = Paths.get(
                                dataDirectory.name,
                                "datastore",
                                "${ApplicationPreferencesFileName}.preferences_pb"
                            )

                            fileName.toFile()
                        }
                    )
                }
            }
            INSTANCE!!
        }
}