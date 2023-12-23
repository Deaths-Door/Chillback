package com.deathsdoor.chillback.data.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.deathsdoor.chillback.ui.providers.LocalSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlin.experimental.ExperimentalTypeInference

class Settings internal constructor(private val context: Context) {
    private companion object {
        private val LOGIN_SKIPPED by lazy { booleanPreferencesKey("login-skipped") }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private suspend fun edit(transform: suspend (MutablePreferences) -> Unit) = context.dataStore.edit(transform)

    @OptIn(ExperimentalTypeInference::class, ExperimentalCoroutinesApi::class)
    private fun<T> get(@BuilderInference transform: suspend (value: Preferences) -> T) : Flow<T> = context.dataStore.data.mapLatest(transform)

    @OptIn(ExperimentalCoroutinesApi::class)
    val showLoginScreen : Flow<Boolean> = hasSkippedLogin.mapLatest {
        !it && Firebase.auth.currentUser == null
    }

    val hasSkippedLogin: Flow<Boolean> get() = get { it[LOGIN_SKIPPED] ?: false }

    suspend fun updateHasSkippedLogin(value : Boolean) = edit {
        it[LOGIN_SKIPPED] = value
    }
}