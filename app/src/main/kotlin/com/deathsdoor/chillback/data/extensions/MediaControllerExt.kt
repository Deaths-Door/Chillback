package com.deathsdoor.chillback.data.extensions

import android.os.Bundle
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.repositories.MusicRepository

private inline val MediaController.mediaItemsRange get() = 0 until mediaItemCount
fun <T> MediaController.mapMediaItems(transform : (MediaItem) -> T) = mediaItemsRange.map { transform(getMediaItemAt(it)) }

fun MediaController.hasNotSameMediaItemsAs(tracks : List<Track>): Boolean {
    var index = 0
    return tracks.any {
        val isNotSame= getMediaItemAt(index).mediaId != tracks[index].id.toString()
        index += 1
        isNotSame
    }
}

fun MediaController.mediaItemOfOrNull(track : Track,transform : (Int,MediaItem) -> Unit) {
    val id = track.id.toString()
    for(index in mediaItemsRange) {
        val mediaItem = getMediaItemAt(index)
        if(mediaItem.mediaId == id) transform(index,mediaItem)
    }
}
suspend fun Collection<Track>.asMediaItems(musicRepository : MusicRepository) = map { it.asMediaItem(musicRepository) }

fun MediaMetadata.Builder.setIsFavourite(isFavorite : Boolean) = setExtras(Bundle().apply { putBoolean("isFavorite",isFavorite) })
fun MediaMetadata.Builder.setIsFavourite(track : Track) = setIsFavourite(track.isFavorite)
fun MediaItem?.isLiked() = this?.mediaMetadata?.extras?.getBoolean("isFavorite") ?: false

@UnstableApi
val PreviewMediaController = object :Player {
    override fun getApplicationLooper(): Looper = Looper.getMainLooper()

    override fun addListener(listener: Player.Listener) = Unit

    override fun removeListener(listener: Player.Listener) = Unit

    override fun setMediaItems(mediaItems: MutableList<MediaItem>) = Unit

    override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) = Unit

    override fun setMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ) = Unit

    override fun setMediaItem(mediaItem: MediaItem) = Unit

    override fun setMediaItem(mediaItem: MediaItem, startPositionMs: Long) = Unit

    override fun setMediaItem(mediaItem: MediaItem, resetPosition: Boolean) = Unit

    override fun addMediaItem(mediaItem: MediaItem) = Unit

    override fun addMediaItem(index: Int, mediaItem: MediaItem) = Unit

    override fun addMediaItems(mediaItems: MutableList<MediaItem>) = Unit

    override fun addMediaItems(index: Int, mediaItems: MutableList<MediaItem>) = Unit

    override fun moveMediaItem(currentIndex: Int, newIndex: Int) = Unit

    override fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int) = Unit

    override fun replaceMediaItem(index: Int, mediaItem: MediaItem) = Unit

    override fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: MutableList<MediaItem>
    ) = Unit

    override fun removeMediaItem(index: Int) = Unit

    override fun removeMediaItems(fromIndex: Int, toIndex: Int) = Unit

    override fun clearMediaItems() = Unit

    override fun isCommandAvailable(command: Int): Boolean = true

    override fun canAdvertiseSession(): Boolean = false

    @OptIn(UnstableApi::class)
    override fun getAvailableCommands(): Player.Commands = Player.Commands.Builder().addAllCommands().build()

    override fun prepare() = Unit

    override fun getPlaybackState(): Int = STATE_READY
    override fun getPlaybackSuppressionReason(): Int = Player.PLAYBACK_SUPPRESSION_REASON_NONE

    private var isPlaying = false
    override fun isPlaying(): Boolean = isPlaying
    override fun getPlayerError(): PlaybackException? = null

    override fun play() {
        isPlaying = true
    }

    override fun pause() {
        isPlaying = false
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) = Unit

    override fun getPlayWhenReady(): Boolean = true

    private var repeatMode = REPEAT_MODE_OFF
    override fun setRepeatMode(value: Int) {
        repeatMode = value
    }

    override fun getRepeatMode(): Int = repeatMode
    private var shuffleModeEnabled = false
    override fun setShuffleModeEnabled(v: Boolean) {
        shuffleModeEnabled = v
    }

    override fun getShuffleModeEnabled(): Boolean = shuffleModeEnabled

    override fun isLoading(): Boolean = false

    override fun seekToDefaultPosition() = Unit

    override fun seekToDefaultPosition(mediaItemIndex: Int) = Unit

    override fun seekTo(positionMs: Long) = Unit

    override fun seekTo(mediaItemIndex: Int, positionMs: Long) = Unit

    override fun getSeekBackIncrement(): Long = 0L

    override fun seekBack() = Unit

    override fun getSeekForwardIncrement(): Long = 0L

    override fun seekForward() = Unit

    @UnstableApi
    override fun hasPrevious(): Boolean = false
    @UnstableApi
    override fun hasPreviousWindow(): Boolean = false

    override fun hasPreviousMediaItem(): Boolean = false
    @UnstableApi
    override fun previous() = Unit
    @UnstableApi
    override fun seekToPreviousWindow() = Unit

    override fun seekToPreviousMediaItem() = Unit

    override fun getMaxSeekToPreviousPosition(): Long = 0L

    override fun seekToPrevious() = Unit

    @UnstableApi
    override fun hasNext(): Boolean = false
    @UnstableApi
    override fun hasNextWindow(): Boolean= false
    @UnstableApi
    override fun hasNextMediaItem(): Boolean = false

    @UnstableApi
    override fun next() = Unit
    @UnstableApi
    override fun seekToNextWindow() = Unit

    override fun seekToNextMediaItem() = Unit

    override fun seekToNext() = Unit

    override fun setPlaybackParameters(playbackParameters: PlaybackParameters) = Unit

    override fun setPlaybackSpeed(speed: Float) = Unit

    override fun getPlaybackParameters(): PlaybackParameters = PlaybackParameters.DEFAULT

    override fun stop() = Unit

    override fun release() = Unit

    override fun getCurrentTracks(): Tracks = Tracks.EMPTY

    override fun getTrackSelectionParameters(): TrackSelectionParameters = TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT

    override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) = Unit

    override fun getMediaMetadata(): MediaMetadata = MediaMetadata.EMPTY

    override fun getPlaylistMetadata(): MediaMetadata= MediaMetadata.EMPTY

    override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) = Unit

    override fun getCurrentManifest(): Any? = null
    override fun getCurrentTimeline(): Timeline = Timeline.EMPTY
    override fun getCurrentPeriodIndex(): Int = 0

    override fun getCurrentWindowIndex(): Int= 0

    override fun getCurrentMediaItemIndex(): Int = 0

    override fun getNextWindowIndex(): Int = 1

    override fun getNextMediaItemIndex(): Int = 1

    override fun getPreviousWindowIndex(): Int = 0

    override fun getPreviousMediaItemIndex(): Int = 0

    override fun getCurrentMediaItem(): MediaItem? {
        TODO("Not yet implemented")
    }

    override fun getMediaItemCount(): Int = 0

    override fun getMediaItemAt(index: Int): MediaItem = MediaItem.EMPTY

    override fun getDuration(): Long = 0

    override fun getCurrentPosition(): Long = 0

    override fun getBufferedPosition(): Long = 0

    override fun getBufferedPercentage(): Int= 0

    override fun getTotalBufferedDuration(): Long = 0

    override fun isCurrentWindowDynamic(): Boolean = false

    override fun isCurrentMediaItemDynamic(): Boolean = false

    override fun isCurrentWindowLive(): Boolean = false

    override fun isCurrentMediaItemLive(): Boolean = false

    override fun getCurrentLiveOffset(): Long=0

    override fun isCurrentWindowSeekable(): Boolean = false
    override fun isCurrentMediaItemSeekable(): Boolean = false

    override fun isPlayingAd(): Boolean = false

    override fun getCurrentAdGroupIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentAdIndexInAdGroup(): Int {
        TODO("Not yet implemented")
    }

    override fun getContentDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getAudioAttributes(): AudioAttributes {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Float) = Unit

    override fun getVolume(): Float {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurface() = Unit

    override fun clearVideoSurface(surface: Surface?) = Unit

    override fun setVideoSurface(surface: Surface?) = Unit

    override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) = Unit

    override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) = Unit

    override fun setVideoSurfaceView(surfaceView: SurfaceView?) = Unit

    override fun clearVideoSurfaceView(surfaceView: SurfaceView?) = Unit

    override fun setVideoTextureView(textureView: TextureView?) = Unit

    override fun clearVideoTextureView(textureView: TextureView?) = Unit

    override fun getVideoSize(): VideoSize {
        TODO("Not yet implemented")
    }

    override fun getSurfaceSize(): Size {
        TODO("Not yet implemented")
    }

    override fun getCurrentCues(): CueGroup {
        TODO("Not yet implemented")
    }

    override fun getDeviceInfo(): DeviceInfo {
        TODO("Not yet implemented")
    }

    override fun getDeviceVolume(): Int {
        TODO("Not yet implemented")
    }

    override fun isDeviceMuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setDeviceVolume(volume: Int) = Unit

    override fun setDeviceVolume(volume: Int, flags: Int) = Unit

    override fun increaseDeviceVolume() = Unit

    override fun increaseDeviceVolume(flags: Int) = Unit

    override fun decreaseDeviceVolume() = Unit

    override fun decreaseDeviceVolume(flags: Int) = Unit

    override fun setDeviceMuted(muted: Boolean) = Unit

    override fun setDeviceMuted(muted: Boolean, flags: Int) = Unit

    override fun setAudioAttributes(audioAttributes: AudioAttributes, handleAudioFocus: Boolean) = Unit

}