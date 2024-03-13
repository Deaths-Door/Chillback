package com.deathsdoor.chillback.data.extensions

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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