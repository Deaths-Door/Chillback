package com.deathsdoor.chillback.data.preferences

import androidx.datastore.preferences.core.stringPreferencesKey
import com.deathsdoor.chillback.data.music.MediaPlaybackPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

private val PLAYBACK by lazy { stringPreferencesKey("music-playback") }

val ApplicationPreferences.playback: Settings<MediaPlaybackPreferences?>
    get () = object : Settings<MediaPlaybackPreferences?> {
        override fun current(): Flow<MediaPlaybackPreferences?> {
            return this@playback.current(key = PLAYBACK,default = { null }) { Json.decodeFromString<MediaPlaybackPreferences>(it) }
        }
        override suspend fun update(value: MediaPlaybackPreferences?) = update(
            key = PLAYBACK,
            value = value
        )
    }