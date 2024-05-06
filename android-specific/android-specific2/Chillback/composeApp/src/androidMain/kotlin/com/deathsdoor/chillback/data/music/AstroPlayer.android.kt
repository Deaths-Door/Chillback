@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.music

import android.content.ComponentName
import androidx.annotation.FloatRange
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.deathsdoor.chillback.data.extensions.applicationContext
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual typealias NativeMediaPLayer = MediaController

@Suppress("UNUSED")
actual open class AstroPlayer actual constructor(private val nativeMediaPlayer: NativeMediaPLayer) {
    actual open fun prepare() = nativeMediaPlayer.prepare()
    actual open fun release() = nativeMediaPlayer.release()

    actual open fun play() = nativeMediaPlayer.play()

    actual open fun pause() = nativeMediaPlayer.pause()

    actual open val isPlaying: Boolean get() = nativeMediaPlayer.isPlaying
    actual open val isPaused: Boolean get() = !nativeMediaPlayer.playWhenReady && !nativeMediaPlayer.isPlaying
    actual open var playBackSpeed: Float = 1f
        protected set(value){
            field = value
            nativeMediaPlayer.setPlaybackSpeed(field)
        }

    actual open var repeatMode : RepeatMode
        get() = nativeMediaPlayer.repeatMode.toRepeatMode()
        set(value) {
            nativeMediaPlayer.repeatMode = value.toPlayerRepeatMode()
        }
    actual open var shuffleModeEnabled : Boolean
        get() = nativeMediaPlayer.shuffleModeEnabled
        set(value) {
            nativeMediaPlayer.shuffleModeEnabled
        }

    actual open val volume: Float get() = nativeMediaPlayer.volume
    @Suppress("DEPRECATION")
    actual open fun increaseVolume() = nativeMediaPlayer.increaseDeviceVolume()
    @Suppress("DEPRECATION")
    actual open fun decreaseVolume() = nativeMediaPlayer.decreaseDeviceVolume()

    actual open fun increaseVolumeBy(@FloatRange(from = 0.0, to = 1.0) offset : Float) {
        nativeMediaPlayer.volume += offset
    }

    actual open fun decreaseVolumeBy(@FloatRange(from = 0.0, to = 1.0)  offset : Float) {
        nativeMediaPlayer.volume -= offset
    }

    actual open val isMuted : Boolean get() = nativeMediaPlayer.isDeviceMuted
    @Suppress("DEPRECATION")
    actual open fun mute() = nativeMediaPlayer.setDeviceMuted(true)
    @Suppress("DEPRECATION")
    actual open fun unMute() = nativeMediaPlayer.setDeviceMuted(false)

    actual open val seekBackIncrement : Long get() = nativeMediaPlayer.seekBackIncrement
    actual open val seekForwardIncrement : Long get() = nativeMediaPlayer.seekForwardIncrement

    actual open fun seekTo(milliseconds: Long) = nativeMediaPlayer.seekTo(milliseconds)
    actual open fun seekToMediaItem(index : Int) = nativeMediaPlayer.seekTo(index,0)
    
    actual open val currentPosition : Long get() = nativeMediaPlayer.currentPosition
    actual open val contentDuration : Long get() = nativeMediaPlayer.contentDuration
    
    actual open val currentMediaItem: AstroMediaItem? get() = currentNativeMediaItem?.asAstroMediaItem()
    final val currentNativeMediaItem: MediaItem? get() = nativeMediaPlayer.currentMediaItem

    actual open val currentMediaItemIndex: Int get() = nativeMediaPlayer.currentMediaItemIndex
    actual open val mediaItemCount: Int get() = nativeMediaPlayer.mediaItemCount

    actual open fun clearMediaItems() = nativeMediaPlayer.clearMediaItems()

    actual open fun addMediaItem(item: AstroMediaItem) = nativeMediaPlayer.addMediaItem(item.asNativeMediaItem())
    actual open fun addMediaItem(
        index: Int,
        item: AstroMediaItem,
    ) = nativeMediaPlayer.addMediaItem(index,item.asNativeMediaItem())

    actual open fun addMediaItems(item: List<AstroMediaItem>) = nativeMediaPlayer.addMediaItems(item.map { it.asNativeMediaItem() })

    actual open fun addMediaItems(
        index: Int,
        item: List<AstroMediaItem>,
    ) = nativeMediaPlayer.addMediaItems(index,item.map { it.asNativeMediaItem() })

    actual open fun moveMediaItem(currentIndex: Int, newIndex: Int) = nativeMediaPlayer.moveMediaItem(currentIndex, newIndex)
    actual open fun moveMediaItems(
        fromIndex: Int,
        toIndex: Int,
        newIndex: Int,
    )  = nativeMediaPlayer.moveMediaItems(fromIndex,toIndex,newIndex)

    actual open fun removeMediaItem(index: Int)  = nativeMediaPlayer.removeMediaItem(index)
    actual open fun removeMediaItems(fromIndex: Int, toIndex: Int)  = nativeMediaPlayer.removeMediaItems(fromIndex, toIndex)

    actual open fun replaceMediaItem(
        index: Int,
        mediaItem: AstroMediaItem,
    )  = nativeMediaPlayer.replaceMediaItem(index, mediaItem.asNativeMediaItem())

    actual open fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: List<AstroMediaItem>,
    ) = nativeMediaPlayer.replaceMediaItems(fromIndex,toIndex,mediaItems.map { it.asNativeMediaItem() })

    private inline val NativeMediaPLayer.mediaItemsRange get() = 0 until mediaItemCount

    actual open fun allMediaItems(): List<AstroMediaItem> = mapMediaItems { it }
    actual open fun <T> mapMediaItems(transform: (AstroMediaItem) -> T) : List<T> =
        nativeMediaPlayer.mediaItemsRange.mapNotNull { index ->
            nativeMediaPlayer.getMediaItemAt(index).asAstroMediaItem()?.let { item -> transform(item) }
        }

    companion object {
        fun createFrom(sessionService: MediaSessionService,customize: MediaController.Builder.() -> MediaController.Builder = { this }): ListenableFuture<MediaController> {
            val sessionToken = SessionToken(applicationContext, ComponentName(applicationContext, sessionService::class.java))
            val controllerFuture = MediaController.Builder(applicationContext, sessionToken).customize().buildAsync()
            controllerFuture.addListener({ controllerFuture.get().prepare() }, MoreExecutors.directExecutor())
            return controllerFuture
        }

        suspend fun createFrom(nativeMediaPlayer: ListenableFuture<NativeMediaPLayer>) = withContext(Dispatchers.IO) {
            AstroPlayer(nativeMediaPlayer.get())
        }
    }
}