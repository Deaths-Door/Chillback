package com.deathsdoor.chillback.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Settings internal constructor(private val context: Context) {
    private companion object {
        private val LOGIN_SKIPPED by lazy { booleanPreferencesKey("login-skipped") }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val hasSkippedLogin: Flow<Boolean>
        get() = context.dataStore.data.map { it[LOGIN_SKIPPED] ?: false }

    suspend fun updateHasSkippedLogin(value : Boolean) = context.dataStore.edit {
        it[LOGIN_SKIPPED] = value
    }
}