package com.deathsdoor.chillback.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// TODO : move to firebase as well
class ApplicationSettings(private val context : Context) {

    fun x() {}
    fun<T> currentLocal(key : Preferences.Key<T>, default : T) : Flow<T> = currentLocal(key, { default }) { it }
    @OptIn(ExperimentalCoroutinesApi::class)
    fun<T,O> currentLocal(key : Preferences.Key<T>, default : () -> O,map : (T) -> O) : Flow<O> = context.dataStore.data
        .mapLatest { preferences -> preferences[key]?.let { map(it) } ?: default() }

    suspend fun<T> updateLocale(key : Preferences.Key<T>,value : T) {
        context.dataStore.edit { it[key] = value }
    }

    val miscellaneous by lazy { MiscellaneousSettings(this) }
    val music by lazy { MusicSettings(this) }

    interface Settings<T> {
        fun current() : Flow<T>
        suspend fun update(value : T)

        companion object {
            val dummyFalse = object : ApplicationSettings.Settings<Boolean> {
                override fun current(): Flow<Boolean> = flow { emit(false) }
                override suspend fun update(value: Boolean) = Unit
            }
            fun<I : Settings<T>,T> CoroutineScope.update(settings : I,value : T) = this@update.launch {
                settings.update(value)
            }
        }
    }
}