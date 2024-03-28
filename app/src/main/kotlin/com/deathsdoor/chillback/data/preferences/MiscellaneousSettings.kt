package com.deathsdoor.chillback.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow


@JvmInline
value class MiscellaneousSettings(private val settings: ApplicationSettings) {
    companion object {
        private val LOGIN_SKIPPED by lazy { booleanPreferencesKey("login-skipped") }
    }

    val skippedLogin get() = object : ApplicationSettings.Settings<Boolean> {
        override fun current(): Flow<Boolean> = settings.currentLocal(LOGIN_SKIPPED,false)
        override suspend fun update(value: Boolean) = settings.updateLocale(LOGIN_SKIPPED,value)
    }
}