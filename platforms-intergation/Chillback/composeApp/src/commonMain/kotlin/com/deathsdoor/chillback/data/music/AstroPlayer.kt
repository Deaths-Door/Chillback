@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.deathsdoor.chillback.data.music

expect class NativeMediaPLayer

@Suppress("UNUSED")
expect open class AstroPlayer constructor(nativeMediaPlayer : NativeMediaPLayer) {
    open fun  prepare()
    open fun  release()

    open fun  play()
    open fun  pause()

    open val  isPlaying : Boolean
    open val  isPaused : Boolean
    open var playBackSpeed : Float
        protected set

    open var repeatMode : RepeatMode
    open var shuffleModeEnabled : Boolean

    open val  volume : Float

    open fun  increaseVolume()
    open fun  decreaseVolume()

    open fun  increaseVolumeBy(offset : Float)
    open fun  decreaseVolumeBy(offset : Float)

    open val  isMuted : Boolean
    open fun  mute()
    open fun  unMute()

    open val  seekBackIncrement : Long
    open val  seekForwardIncrement : Long

    open fun  seekTo(milliseconds: Long)
    open fun  seekToMediaItem(index : Int)
    
    open val  currentPosition : Long
    open val  contentDuration : Long

    open val  currentMediaItem : AstroMediaItem?
    open val  currentMediaItemIndex : Int

    open val  mediaItemCount : Int

    open fun  clearMediaItems()

    open fun addMediaItem(item : AstroMediaItem)
    open fun addMediaItem(index : Int,item : AstroMediaItem)

    open fun addMediaItems(item : List<AstroMediaItem>)
    open fun addMediaItems(index : Int,item : List<AstroMediaItem>)

    open fun moveMediaItem(currentIndex: Int, newIndex: Int)
    open fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int)

    open fun removeMediaItem(index: Int)
    open fun removeMediaItems(fromIndex: Int, toIndex: Int)

    open fun replaceMediaItem(index: Int, mediaItem: AstroMediaItem)
    open fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: List<AstroMediaItem>,
    )


    open fun allMediaItems() : List<AstroMediaItem>
    open fun<T> mapMediaItems(transform :(AstroMediaItem) -> T) : List<T>
}