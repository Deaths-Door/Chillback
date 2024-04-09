package com.deathsdoor.chillback.data.music

expect suspend fun<T> AstroPlayer.withContext(value : suspend () -> T) : T

fun AstroPlayer.seekForwardBy(milliseconds: Long) = seekTo(currentPosition + milliseconds)
fun AstroPlayer.seekBackwardBy(milliseconds: Long) = seekTo(currentPosition - milliseconds)

fun AstroPlayer.seekForwardByIncrement() = seekTo(currentPosition + seekForwardIncrement)
fun AstroPlayer.seekBackwardByIncrement() = seekTo(currentPosition - seekBackIncrement)

fun AstroPlayer.seekToStartOfMediaItem() = seekTo(0)
fun AstroPlayer.seekToEndOfMediaItem() = seekTo(contentDuration)

fun AstroPlayer.seekToMediaItemThenAt(index : Int,milliseconds: Long){
    seekToMediaItem(index)
    seekTo(milliseconds)
}

fun AstroPlayer.seekToNextMediaItem() {
    if(hasNextMediaItem) seekToMediaItem(currentMediaItemIndex + 1)
}
fun AstroPlayer.seekToPrevious() {
    if(hasPreviousMediaItem) seekToMediaItem(currentMediaItemIndex - 1)
}

val AstroPlayer.currentMediaMetadata get() = currentMediaItem?.metadata

val AstroPlayer.hasNextMediaItem : Boolean get() = currentMediaItemIndex < mediaItemCount - 1
val AstroPlayer.hasPreviousMediaItem : Boolean get() = currentMediaItemIndex > 0


fun AstroPlayer.clearThenAddMediaItem(item : AstroMediaItem) {
    clearMediaItems()
    addMediaItem(item)
}

fun AstroPlayer.clearThenAddMediaItem(index : Int,item : AstroMediaItem) {
    clearMediaItems()
    addMediaItem(index,item)
}

fun AstroPlayer.clearThenAddMediaItems(item : List<AstroMediaItem>) {
    clearMediaItems()
    addMediaItems(item)
}
fun AstroPlayer.clearThenAddMediaItems(index : Int,item : List<AstroMediaItem>){
    clearMediaItems()
    addMediaItems(index,item)
}