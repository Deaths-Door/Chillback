package com.deathsdoor.chillback.data.media

import androidx.media3.common.Player.RepeatMode
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.extensions.asMediaItems
import com.deathsdoor.chillback.data.extensions.mapMediaItems
import com.deathsdoor.chillback.data.repositories.MusicRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
class MediaPlaybackPreferences private constructor(
    private val queue : List<Long>,
    private val currentMediaItemIndex : Int,
    private val shuffleModeEnabled : Boolean,
    private val repeatMode : @RepeatMode Int,
) {
    suspend fun apply(
        database: ApplicationLocalDatabase,
        musicRepository : MusicRepository,
        mediaController: MediaController
    ) : MediaController = mediaController.apply {
        shuffleModeEnabled = this@MediaPlaybackPreferences.shuffleModeEnabled
        repeatMode = this@MediaPlaybackPreferences.repeatMode

        addMediaItems(database.trackDao.tracksFrom(this@MediaPlaybackPreferences.queue).asMediaItems(musicRepository))

        delay(500)

        seekTo(this@MediaPlaybackPreferences.currentMediaItemIndex,0)
    }


    companion object {
        fun from(mediaController: MediaController): MediaPlaybackPreferences =
            MediaPlaybackPreferences(
                queue = mediaController.mapMediaItems { it.mediaId.toLong() },
                currentMediaItemIndex = mediaController.currentMediaItemIndex,
                shuffleModeEnabled = mediaController.shuffleModeEnabled,
                repeatMode = mediaController.repeatMode
            )
    }
}