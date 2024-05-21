package com.deathsdoor.chillback.core.media.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.seekToStartOfMediaItem
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun Modifier.optionsItemSpacing() = padding(horizontal = 24.dp,vertical = 12.dp)

private fun Boolean.toInt() = if (this) 1 else 0

internal fun onMediaItemClick(
    astroPlayer: AstroPlayer,
    coroutineScope : CoroutineScope,
    mediaItem : AstroMediaItem,
    mediaItems : List<AstroMediaItem>,
    addAsNextMediaItem : Boolean = false
) {
    val trackId = mediaItem.mediaId

    // If current track is playing , start from default position
    if (astroPlayer.currentMediaItem?.mediaId == trackId) {
        astroPlayer.seekToStartOfMediaItem()
        astroPlayer.play()
    }

    val mediaItemCount = astroPlayer.mediaItemCount

    /// This means this queue is not considered part of, (or is) the track list
    if (
        mediaItems.size != mediaItemCount ||
        astroPlayer.allMediaItems().zip(mediaItems).any { (a,b) -> a.mediaId != b.mediaId }
    ) {
        // This means this queue is definitely not part of this track list , hence add it
        coroutineScope.launch {
            val currentMediaItemIndex = astroPlayer.currentMediaItemIndex

            astroPlayer.addMediaItem(
                index = currentMediaItemIndex + addAsNextMediaItem.toInt(),
                item = mediaItem
            )
        }
    }

    val index = mediaItems.indexOf(mediaItem)
    astroPlayer.seekToMediaItem(index)
}