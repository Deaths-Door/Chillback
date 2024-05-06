@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.music

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.media.Meta
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import java.time.Clock
import java.util.Locale
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


actual typealias NativeMediaPLayer = MediaPlayer

@Suppress("UNUSED")
actual open class AstroPlayer actual constructor(private var nativeMediaPlayer: NativeMediaPLayer) {
    @Suppress("UNUSED")
    constructor() : this(nativeMediaPlayer())

    init {
        NativeDiscovery().discover()

        /*nativeMediaPlayer.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
            override fun seekableChanged(mediaPlayer: MediaPlayer?, newSeekable: Int) {
                super.seekableChanged(mediaPlayer, newSeekable)
            }

            override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
                super.mediaChanged(mediaPlayer, media)

            }
        })*/
    }

    actual open fun prepare() = Unit
    actual open fun release() = nativeMediaPlayer.release()

    actual open fun play() = nativeMediaPlayer.controls().play()

    actual open fun pause() = nativeMediaPlayer.controls().pause()

    actual open val isPlaying: Boolean get() = nativeMediaPlayer.status().isPlaying
    actual open val isPaused: Boolean get() = !nativeMediaPlayer.status().isPlaying && nativeMediaPlayer.media().info().audioTracks().size >= 1
    actual open var playBackSpeed: Float
        get() = nativeMediaPlayer.status().rate()
        protected set(value) {
            nativeMediaPlayer.controls().setRate(value)
        }

    private val rawVolume = nativeMediaPlayer.audio().volume()
    actual open val volume: Float get() = rawVolume.toFloat()

    actual open fun increaseVolume() {
        nativeMediaPlayer.audio().setVolume(rawVolume + 10)
    }

    actual open fun decreaseVolume() {
        nativeMediaPlayer.audio().setVolume(rawVolume - 10)
    }

    actual open fun increaseVolumeBy(offset : Float) {
        nativeMediaPlayer.audio().setVolume(rawVolume + offset.toInt())
    }

    actual open fun decreaseVolumeBy(offset : Float) {
        nativeMediaPlayer.audio().setVolume(rawVolume - offset.toInt())
    }

    actual open val isMuted : Boolean get() = nativeMediaPlayer.audio().isMute

    actual open fun mute() {
        nativeMediaPlayer.audio().isMute = true
    }

    actual open fun unMute() {
        nativeMediaPlayer.audio().isMute = false
    }

    private var _seekBackIncrement : Long = 5_000
    private var _seekForwardIncrement : Long = 5_000

    actual open val seekBackIncrement : Long get() = _seekBackIncrement
    actual open val seekForwardIncrement : Long get() = _seekForwardIncrement

    open fun seekBackIncrement(value : Long) {
        _seekBackIncrement = value
    }

    open fun seekForwardIncrement(value : Long) {
        _seekForwardIncrement = value
    }

    actual open fun seekTo(milliseconds: Long) = nativeMediaPlayer.controls().setTime(milliseconds)

    actual open val currentPosition : Long get() = nativeMediaPlayer.status().time()
    actual open val contentDuration : Long get() = nativeMediaPlayer.status().length()

    // From this point on alot of code is inspired form https://github.com/caprica/choonio/blob/master/choonio-core
    private val lock = ReentrantReadWriteLock()
    private val mediaItems = mutableListOf<AstroMediaItem>()
    private val _currentMediaItemIndex : Int = -1

    // TODO : Implement all these functions in the future
    actual open var repeatMode: RepeatMode = RepeatMode.Off
        set(value) = lock.write {
            repeatMode = value
        }

    actual open var shuffleModeEnabled: Boolean = false
        set(value) =  lock.write {
            shuffleModeEnabled = value
        }

    private fun playMediaItem(item : AstroMediaItem) {
        val mediaApi = nativeMediaPlayer.media()
        nativeMediaPlayer.media().meta()
        mediaApi.newMedia()//.meta().set(Meta.ACTORS,"")
        nativeMediaPlayer.media().play(item.source.toString())

    }

    actual open fun seekToMediaItem(index: Int) {
        lock.read {
            mediaItems.getOrNull(index)?.let {
                playMediaItem(it)
            }
        }
    }

    actual open val currentMediaItem: AstroMediaItem? get() = lock.read { mediaItems.getOrNull(_currentMediaItemIndex) }
    actual open val currentMediaItemIndex: Int get() = lock.read { _currentMediaItemIndex }
    actual open val mediaItemCount: Int get() =  lock.read { mediaItems.size  }

    actual open fun clearMediaItems() {
    }

    actual open fun addMediaItem(item: AstroMediaItem){
        lock.write {
            mediaItems.add(item);

        }
    }

    actual open fun addMediaItem(
        index: Int,
        item: AstroMediaItem,
    ) {
        lock.write {
            mediaItems.add(index,item);
        }
    }

    actual open fun addMediaItems(item: List<AstroMediaItem>) {
        lock.write {
            mediaItems.addAll(item);
        }
    }

    actual open fun addMediaItems(
        index: Int,
        item: List<AstroMediaItem>,
    ) {
        lock.write {
            mediaItems.addAll(index,item);
        }
    }

    actual open fun moveMediaItem(currentIndex: Int, newIndex: Int) {
    }

    actual open fun moveMediaItems(
        fromIndex: Int,
        toIndex: Int,
        newIndex: Int,
    ) {
    }

    actual open fun removeMediaItem(index: Int) {
    }

    actual open fun removeMediaItems(fromIndex: Int, toIndex: Int) {
    }

    actual open fun replaceMediaItem(
        index: Int,
        mediaItem: AstroMediaItem,
    ) {
    }

    actual open fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: List<AstroMediaItem>,
    ) {
    }

    actual open fun allMediaItems(): List<AstroMediaItem> = mediaItems
    actual open fun <T> mapMediaItems(transform: (AstroMediaItem) -> T): List<T> = mediaItems.map(transform)

    private companion object {
        private val isMacOS = run {
            val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
            os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0
        }

        fun nativeMediaPlayer(): EmbeddedMediaPlayer = when {
            isMacOS -> {
                CallbackMediaPlayerComponent().mediaPlayer()
            }
            else -> {
                EmbeddedMediaPlayerComponent().mediaPlayer()
            }
        }
    }
}

