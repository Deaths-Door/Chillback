package com.deathsdoor.chillback.data.music
import com.benasher44.uuid.Uuid
import com.deathsdoor.chillback.components.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.data.database.ApplicationDatabase
import com.deathsdoor.chillback.data.database.tracksFromUuids
import com.deathsdoor.chillback.data.extensions.UuidSerializer
import com.deathsdoor.chillback.data.extensions.asMediaItemsOrReport
import com.deathsdoor.chillback.data.repositories.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
class MediaPlaybackPreferences private constructor(
    private val queue : List<@Serializable(with = UuidSerializer::class) Uuid>,
    private val currentMediaItemIndex : Int,
    private val shuffleModeEnabled : Boolean,
    private val repeatMode : RepeatMode,
) {
    suspend fun apply(
        musicRepository : MusicRepository,
        astroPlayer: AstroPlayer,
        stackedSnackbarHostState: StackableSnackbarState
    ) : AstroPlayer = withContext(Dispatchers.IO) {
        val mediaItems = ApplicationDatabase
            .tracksFromUuids(this@MediaPlaybackPreferences.queue)
            .asMediaItemsOrReport(musicRepository,stackedSnackbarHostState)

        astroPlayer.withContext {
            astroPlayer.apply {
                shuffleModeEnabled = this@MediaPlaybackPreferences.shuffleModeEnabled
                repeatMode = this@MediaPlaybackPreferences.repeatMode

                addMediaItems(mediaItems)

                delay(500)
                seekToMediaItemThenAt(this@MediaPlaybackPreferences.currentMediaItemIndex,0)
            }
        }
    }

    companion object {
        fun from(mediaController: AstroPlayer): MediaPlaybackPreferences = MediaPlaybackPreferences(
            queue = mediaController.mapMediaItems { Uuid.fromString(it.mediaId) },
            currentMediaItemIndex = mediaController.currentMediaItemIndex,
            shuffleModeEnabled = mediaController.shuffleModeEnabled,
            repeatMode = mediaController.repeatMode
        )
    }
}