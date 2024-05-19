package com.deathsdoor.chillback.core.preferences

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import okio.Path.Companion.toPath


/**
 *  An object for managing application preferences using both local storage and Firebase Realtime Database.
 */
@OptIn(ExperimentalCoroutinesApi::class)
object ApplicationPreference {
    init {
        Firebase.database.setPersistenceEnabled(true)
    }

    private val datastore = createDataStore()

    /**
     * Retrieves a preference value from local storage with transformation.
     *
     * @param key The key of the preference to retrieve.
     * @param default The default value to return if the preference is not found.
     * @param map A lambda function to transform the retrieved value before returning it.
     * @return A Flow that emits the transformed preference value or the default value if not found.
     */
    fun<T> currentLocal(key : Preferences.Key<T>, default : T) : Flow<T> = currentLocal(key,default) { it }

    /**
     * Retrieves a preference value from local storage.
     *
     * @param key The key of the preference to retrieve.
     * @param default The default value to return if the preference is not found.
     * @return A Flow that emits the retrieved preference value or the default value if not found.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun<T,O> currentLocal(key : Preferences.Key<T>, default : O, map : (T) -> O) : Flow<O> = datastore.data
        .mapLatest { preferences -> preferences[key]?.let { map(it) } ?: default }

    /**
     * Updates a preference value in local storage.
     *
     * @param key The key of the preference to update.
     * @param value The new value for the preference.
     * @throws SuspensionException If an error occurs while updating the preference.
     */
    suspend fun<T> updateLocale(key : Preferences.Key<T>,value : T) {
        datastore.edit { it[key] = value }
    }

    /**
     * Retrieves a preference value from Firebase Realtime Database (internal use only).
     *
     * @param key The key of the preference to retrieve.
     * @throws IllegalStateException If there is no current authenticated user.
     * @return A DatabaseReference pointing to the preference location in the database.
     */
    @Suppress("FunctionName")
    @PublishedApi
    internal fun<T> _currentOnline(key : Preferences.Key<T>) : DatabaseReference {
        requireNotNull(Firebase.auth.currentUser)
        return Firebase.database.reference("${Firebase.auth.currentUser!!.uid}/preferences")
            .child(key.name)
    }

    /**
     * Retrieves a preference value from Firebase Realtime Database with transformation.
     *
     * @param key The key of the preference to retrieve.
     * @param default The default value to return if the preference is not found.
     * @param map A lambda function to transform the retrieved value before returning it.
     * @return A Flow that emits the transformed preference value or the default value if not found.
     */
    fun<T,O> currentOnline(key : Preferences.Key<T>,default : O,map : (T) -> O): Flow<O> = _currentOnline(key).valueEvents.mapLatest {
        it.value?.let {
            @Suppress("UNCHECKED_CAST")
            map(it as T)
        } ?: default
    }

    /**
     * Updates a preference value in Firebase Realtime Database.
     *
     * @param key The key of the preference to update.
     * @param value The new value for the preference.
     */
    suspend inline fun<reified T> updateOnline(key : Preferences.Key<T>, value : T) {
        _currentOnline(key).child(key.name).setValue(value)
    }

    /**
     * Retrieves the preferred preference value, prioritizing online storage if a user is signed in.
     *
     * @param key The key of the preference to retrieve.
     * @param default The default value to return if the preference is not found in either storage.
     * @param map A lambda function to transform the retrieved value before returning it.
     * @return A Flow that emits the transformed preference value or the default value if not found.
     */
    fun<T,O> preferredCurrent(key : Preferences.Key<T>, default : O, map : (T) -> O) {
        if(Firebase.auth.currentUser == null) currentLocal(key,default,map)
        else currentOnline(key,default,map)
    }

    /**
     * Updates the preferred preference value, prioritizing online storage if a user is signed in.
     *
     * @param key The key of the preference to update.
     * @param value The new value for the preference.
     */
    suspend inline fun<reified T> preferredUpdate(key : Preferences.Key<T>, value : T) {
        if(Firebase.auth.currentUser == null) updateLocale(key,value)
        else updateOnline(key,value)
    }
}