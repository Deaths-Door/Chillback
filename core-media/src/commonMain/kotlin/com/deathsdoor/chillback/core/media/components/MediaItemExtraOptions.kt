package com.deathsdoor.chillback.core.media.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.media.components.action.PlayNextThumbItem
import com.deathsdoor.chillback.core.media.components.action.PlayNowThumbItem
import com.deathsdoor.chillback.core.media.extensions.onMediaItemClick
import kotlinx.coroutines.CoroutineScope

// Keep this sync with TrackItem::MoreInfoButtonContent
// Play Now
// Play Next
// Add to Playback Queue -> TODO
// Add to playlist -> TODO
// Metadata and Edit -> TODO
// Share -> TODO
// Set As Ringtone -> TODO
// Lyrics -> TODO
// Search In Youtube / Other Services -> TODO
// Delete / Remove from list -> TODO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MediaItemExtraOptions(
    astroPlayer: AstroPlayer,
    coroutineScope: CoroutineScope,
    mediaItem: AstroMediaItem,
    mediaItems : List<AstroMediaItem>,
    onDismiss : () -> Unit
) = ModalBottomSheet(onDismissRequest =  onDismiss) {
    PlayNowThumbItem(
        onClick = {
            onMediaItemClick(
                astroPlayer = astroPlayer,
                coroutineScope = coroutineScope,
                mediaItem = mediaItem,
                mediaItems = mediaItems
            )
        }
    )

    PlayNextThumbItem(
        onClick = {
            onMediaItemClick(
                astroPlayer = astroPlayer,
                coroutineScope = coroutineScope,
                mediaItem = mediaItem,
                mediaItems = mediaItems,
                addAsNextMediaItem = true
            )
        }
    )


}