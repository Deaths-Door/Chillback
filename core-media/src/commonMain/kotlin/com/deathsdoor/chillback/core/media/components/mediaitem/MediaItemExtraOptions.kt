package com.deathsdoor.chillback.core.media.components.mediaitem

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.components.VerticalMediaItemTitleWithArtists
import com.deathsdoor.chillback.core.media.components.action.AddToQueueThumbItem
import com.deathsdoor.chillback.core.media.components.action.DeleteThumbItem
import com.deathsdoor.chillback.core.media.components.action.PlayNextThumbItem
import com.deathsdoor.chillback.core.media.components.action.PlayNowThumbItem
import com.deathsdoor.chillback.core.media.components.action.RingtoneSelectorThumbItem
import com.deathsdoor.chillback.core.media.components.action.ShareThumbItem
import com.deathsdoor.chillback.core.media.extensions.onMediaItemClick
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import com.deathsdoor.chillback.core.media.resources.Res

// Keep this sync with TrackItem::MoreInfoButtonContent
// Play Now
// Play Next
// Add to Playback Queue
// TODO:Add to playlist
// TODO:Metadata and Edit
// Share
// Set As Ringtone
// TODO:Lyrics
// TODO:Search In Youtube / Other Services
// Delete / Remove from list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MediaItemExtraOptions(
    astroPlayer: AstroPlayer,
    coroutineScope: CoroutineScope,
    mediaItem: AstroMediaItem,
    mediaItems : List<AstroMediaItem>,
    onRemove : ((AstroMediaItem) -> Unit)?,
    onDismiss : () -> Unit,
) = ModalBottomSheet(onDismissRequest =  onDismiss) {
    TrackArtwork(
        modifier = Modifier
            .padding(12.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        mediaItem = mediaItem
    )

    VerticalMediaItemTitleWithArtists(
        mediaItem = mediaItem,
        titleStyle = MaterialTheme.typography.titleSmall,
        artistStyle = MaterialTheme.typography.bodyMedium
    )

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

    AddToQueueThumbItem(astroPlayer = astroPlayer, mediaItem = mediaItem)

    RingtoneSelectorThumbItem(applicationPlayer = astroPlayer, mediaItem = mediaItem)


    // TODO;add the rest

    ShareThumbItem(mediaItem = mediaItem)

    val name = mediaItem.metadata?.title ?: "Unknown"

    onRemove?.let {
        DeleteThumbItem(
            label = stringResource(Res.strings.remove_track_from_collection),
            name = name,
            onDelete = {
                it(mediaItem)
            }
        )
    }

    DeleteThumbItem(
        label = stringResource(Res.strings.delete_track_from_device),
        name = name,
        onDelete = {
            // TODO; delete track from the database
        }
    )

    Spacer(modifier = Modifier.height(24.dp))
}