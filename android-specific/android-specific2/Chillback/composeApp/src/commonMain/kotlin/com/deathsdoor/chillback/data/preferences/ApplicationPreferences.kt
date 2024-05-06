@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal const val ApplicationPreferencesFileName = "settings"

expect class ApplicationPreferences constructor(){
    val datastore : DataStore<Preferences>
}

