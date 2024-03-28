package com.deathsdoor.chillback.data.preferences

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.media.MediaPlaybackPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@JvmInline
value class MusicSettings(private val settings: ApplicationSettings) {
    companion object {
        private val PLAYBACK by lazy { stringPreferencesKey("music-playback") }
    }

    fun playback(mediaController: MediaController) = object : ApplicationSettings.Settings<MediaPlaybackPreferences> {
        override fun current(): Flow<MediaPlaybackPreferences> = settings.currentLocal(
            key = PLAYBACK,
            default = { MediaPlaybackPreferences.from(mediaController) },
            map = { Json.decodeFromString(it) }
        )

        override suspend fun update(value: MediaPlaybackPreferences) = settings.updateLocale(
            key = PLAYBACK,
            value = Json.encodeToString(value)
        )
    }
}
