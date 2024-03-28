package com.deathsdoor.chillback.data.settings

import android.content.Context
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.deathsdoor.chillback.data.mediaplayer.MediaPlaybackPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale

// TODO : Migrate from local settinsg to firebase when user logins in for first time
class Settings internal constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val firebaseGroup by lazy { Firebase.database.reference.child(Firebase.auth.currentUser!!.uid).child("settings") }//.child("${Firebase.auth.currentUser!!) }

    private suspend fun<T> editLocal(key : Preferences.Key<T>,value : T) = context.dataStore.edit { it[key] = value }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun<T> getLocal(key : Preferences.Key<T>, default : T) : Flow<T> = context.dataStore.data.mapLatest { it[key] ?: default }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun<T> getOnline(child : String,clazz : Class<T>, default : T) : Flow<T> = firebaseGroup.child(child).snapshots.mapLatest { it.getValue(clazz) ?: default }
    private suspend fun editOnline(child : String,value : Any) = firebaseGroup.child(child).setValue(value).await()

    fun<T> get(
        key : Preferences.Key<T>,
        clazz : Class<T>,
        default : T,
    ) = if(Firebase.auth.currentUser == null) getLocal(key,default) else getOnline(key.name,clazz,default)

    suspend fun<T : Any> update(
        key : Preferences.Key<T>,
        value : T
    ) = if(Firebase.auth.currentUser == null) editLocal(key,value) else editOnline(key.name,value)

    val appLocales: LocaleList
        @Composable
        @ReadOnlyComposable
        get() = LocalContext.current.resources.configuration.locales

    val currentLocale: Locale
        @Composable
        @ReadOnlyComposable
        get() = LocalConfiguration.current.locales[0]

    companion object {
        private val LOGIN_SKIPPED by lazy { booleanPreferencesKey("login-skipped") }

        val AUTO_LOAD_LYRICS by lazy { booleanPreferencesKey("auto-load-lyrics") }
        val AUTO_LOAD_ALBUM_ART by lazy { booleanPreferencesKey("auto-load-album-art") }
        val AUTO_LOAD_METADATA by lazy { booleanPreferencesKey("auto-load-metadata") }
        val AUTO_PLAY_HEADSET_CONNECT by lazy { booleanPreferencesKey("autoplay-headset-connect") }

        val MOBILE_DATA_STREAMING by lazy { booleanPreferencesKey("streaming-mobile-data") }
        val MOBILE_DATA_DOWNLOAD by lazy { booleanPreferencesKey("download-mobile-data") }

        val GAPLESS_PLAYBACK by lazy { booleanPreferencesKey("playback-gapless") }
        val NORMALIZE_VOLUME by lazy { booleanPreferencesKey("volume-normalize") }

        private val SLEEP_TIMER by lazy { stringPreferencesKey("sleep-timer") }

        val SHOW_LOCAL_LIBRARY by lazy { booleanPreferencesKey("library-local-show") }
        val FILTER_BY_DURATION by lazy { intPreferencesKey("library-local-filter-songs-by-duration") }

        private val MEDIA_PLAYBACK_PREFERENCES by lazy { stringPreferencesKey("playback-preferences") }
    }

    private val hasSkippedLogin  = getLocal(LOGIN_SKIPPED,false)
    suspend fun updateHasSkippedLogin(value : Boolean) = editLocal(LOGIN_SKIPPED,value)

    @OptIn(ExperimentalCoroutinesApi::class)
    val showLoginScreen = hasSkippedLogin.mapLatest { !it && Firebase.auth.currentUser == null }

    val autoLoadLyrics = get(key = AUTO_LOAD_LYRICS, clazz = Boolean::class.java,default = true)
    val autoLoadAlbumArt = get(key = AUTO_LOAD_ALBUM_ART, clazz = Boolean::class.java,default = true)
    val autoLoadMetadata = get(key = AUTO_LOAD_METADATA, clazz = Boolean::class.java,default = true)
    val autoplayOnHeadsetConnection = get(key = AUTO_PLAY_HEADSET_CONNECT, clazz = Boolean::class.java,default = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val sleepTimer : Flow<SleepTimer> = get(
        key = SLEEP_TIMER,
        clazz = String::class.java,
        default = SleepTimer.Disabled.toString()
    ).mapLatest { Json.decodeFromString(it) }

    suspend fun updateSleepTimer(value : SleepTimer) {
        value.start()
        update(SLEEP_TIMER,Json.encodeToString(value))
    }

    val streamingOverMobileData = get(key= MOBILE_DATA_STREAMING, clazz = Boolean::class.java,default = false)
    val downloadOverMobileData = get(key= MOBILE_DATA_STREAMING, clazz = Boolean::class.java,default = false)

    val gaplessPlayback = get(key= GAPLESS_PLAYBACK, clazz = Boolean::class.java,default = false)
    val normalizeVolume = get(key= NORMALIZE_VOLUME, clazz = Boolean::class.java,default = false)

    val showLocalLibrary = get(key= SHOW_LOCAL_LIBRARY, clazz = Boolean::class.java,default = false)
    val filterLocalLibrarySongsByDuration = get(key= FILTER_BY_DURATION, clazz = Int::class.java,default = 30)

    @OptIn(ExperimentalCoroutinesApi::class)
    val mediaPlaybackPreferences : Flow<MediaPlaybackPreferences> = get(
        key = MEDIA_PLAYBACK_PREFERENCES,
        clazz = String::class.java,
        default = Json.encodeToString(MediaPlaybackPreferences())
    ).mapLatest { Json.decodeFromString(it) }

    suspend fun updateMediaPlaybackPreferences(value : MediaPlaybackPreferences) = update(MEDIA_PLAYBACK_PREFERENCES,Json.encodeToString(value))
}