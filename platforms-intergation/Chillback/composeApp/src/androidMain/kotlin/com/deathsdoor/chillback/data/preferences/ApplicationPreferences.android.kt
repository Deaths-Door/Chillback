@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.preferences

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.deathsdoor.chillback.data.extensions.applicationContext
import kotlin.properties.ReadOnlyProperty

actual class ApplicationPreferences {
    private val Context.androidDataStore by provideDatastorePrerequisites<ReadOnlyProperty<Context, DataStore<Preferences>>>{ corruptionHandler, scope ->
        preferencesDataStore(
            name = ApplicationPreferencesFileName,
            corruptionHandler = corruptionHandler,
            scope = scope,
        )
    }

    actual val datastore: DataStore<Preferences> get() = applicationContext.androidDataStore
}