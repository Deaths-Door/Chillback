package com.deathsdoor.chillback.feature.welcome

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.core.preferences.Settings
import kotlinx.coroutines.flow.Flow

val ApplicationPreference.hasSkippedLogin: Settings<Boolean> get() = object: Settings<Boolean> {
    private val key = booleanPreferencesKey("skipped_login")
    override fun current(): Flow<Boolean> = currentLocal(key,false)
    override suspend fun update(value: Boolean) = updateLocale(key,value)
}