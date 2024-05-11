package com.deathsdoor.chillback.feature.welcome

import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.core.preferences.Settings
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
val ApplicationPreference.hasSkippedLogin: Settings<Boolean> get() = object: Settings<Boolean> {
    override fun current(): Flow<Boolean> = currentLocal {
        it.getBooleanFlow("skipped_login",false)
    }

    override suspend fun update(value: Boolean) = updateLocal { settings ->
        settings.putBoolean("skipped_login",value)
    }
}