package com.deathsdoor.chillback.data.preferences

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.deathsdoor.chillback.data.database.ApplicationDatabase.showDataLocally
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.typeOf

/// Source Code copied from androidx.datastore.preferences.preferencesDataStore
fun<T> provideDatastorePrerequisites(provided : (corruptionHandler : ReplaceFileCorruptionHandler<Preferences>,scope: CoroutineScope) -> T) : T {
    return provided(
        ReplaceFileCorruptionHandler { emptyPreferences() },
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun<T,O> ApplicationPreferences.currentLocal(key : Preferences.Key<T>, default : () -> O, map : (T) -> O) : Flow<O> = this.datastore.data
    .mapLatest { preferences -> preferences[key]?.let { map(it) } ?: default() }

suspend fun<T> ApplicationPreferences.updateLocale(key : Preferences.Key<T>,value : T) {
    this.datastore.edit { it[key] = value }
}

@PublishedApi
internal fun firebasePreferencesReference() = Firebase.database.reference()
    .child("settings/${Firebase.auth.currentUser!!.uid}")

@OptIn(ExperimentalCoroutinesApi::class)
fun<T,O> ApplicationPreferences.current(key: Preferences.Key<T>, default: () -> O, map: (T) -> O) : Flow<O> {
    return if(showDataLocally) this.datastore.data.mapLatest { preferences -> preferences[key]?.let { map(it) } ?: default() }
        else firebasePreferencesReference()
            .child(key.name)
            .valueEvents
            .mapLatest { snapshot ->
                snapshot.value?.let { it as O } ?: default()
            }
}


suspend inline fun<reified O> ApplicationPreferences.update(
    key : Preferences.Key<String>,
    value : O
) {
    when {
        showDataLocally -> {
            datastore.edit {
                it[key] = serializerOrNull(typeOf<O>())?.let {
                    Json.encodeToString(value)
                } ?: value.toString()
            }
        }
        else -> firebasePreferencesReference()
            .child(key.name)
            .setValue(value)
    }
}

