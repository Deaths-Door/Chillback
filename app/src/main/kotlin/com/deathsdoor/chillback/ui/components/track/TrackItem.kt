package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.action.AddToQueueDropDownItem
import com.deathsdoor.chillback.ui.components.action.DeleteDropDownItem
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
import com.deathsdoor.chillback.ui.components.action.PlayNexDropDownItem
import com.deathsdoor.chillback.ui.components.action.PlayNowDropDownItem
import com.deathsdoor.chillback.ui.components.action.RingtoneSelectorDropDownItem
import com.deathsdoor.chillback.ui.components.action.ShareThumbItem
import com.deathsdoor.chillback.ui.components.action.TrackMetadataDropDownItem
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnail
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnailCard
import com.deathsdoor.chillback.ui.components.layout.ThumbnailCaption
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.components.layout.applyToggleableOnSelection
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.dragselectcompose.core.DragSelectState

@Composable
@NonRestartableComposable
fun TrackCard(
    modifier : Modifier = Modifier,
    mediaController: Player?,
    track: Track,
    tracks : List<Track>,
    details : TrackDetails?,
    isSelected : Boolean?,
    draggableState : DragSelectState<Track>,
    onRemove : ((Track) -> Unit)?,
    ) {
    val textModifier = Modifier.padding(start = 16.dp)

    SelectableThumbnailCard(
        modifier = modifier.applyToggleableOnSelection(
            item = track,
            isSelected = isSelected,
            draggableState = draggableState
        ),
        isSelected = isSelected,
        title = {
            ThumbnailTitle(
                modifier = textModifier,
                text = details?.name ?: "Wait a second",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        artwork = {
            ArtworkWithFailureInformer(
                modifier = Modifier
                    .padding(12.dp)
                    .matchParentSize(),
                model = details?.artwork,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        },
        caption  = {
            details?.artistWithOthers()?.let { ThumbnailCaption(text = it) }
        },
        actionIcon = {  actionModifier , enabled ->
            LikeButton(
                modifier = actionModifier,
                mediaController = mediaController,
                enabled = enabled,
                track = track,
            )

            MoreInfoButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
                content = {
                    MoreInfoButtonContent(track,tracks,details,onRemove)
                }
            )
        },
    )
}

@Composable
@NonRestartableComposable
fun TrackRowItem(
    modifier : Modifier = Modifier,
    mediaController: Player?,
    track: Track,
    tracks : List<Track>,
    details : TrackDetails?,
    isSelected : Boolean?,
    draggableState : DragSelectState<Track>,
    onRemove : ((Track) -> Unit)?,
) = SelectableThumbnail(
    modifier = modifier
        .fillMaxWidth()
        .applyToggleableOnSelection(
            item = track,
            isSelected = isSelected,
            draggableState = draggableState
        ),
    isSelected = isSelected,
    title = {
        ThumbnailTitle(
            text = details?.name ?: "Wait a second..",
            style = MaterialTheme.typography.bodyMedium
        )
    },
    artwork = {
        ArtworkWithFailureInformer(
            modifier = Modifier.size(64.dp),
            model = details?.artwork,
            contentDescription = null,
        )
    },
    caption  = {
        details?.artistWithOthers()?.let { ThumbnailCaption(text = it) }
    },
    actionIcon = {  actionModifier , enabled ->
        LikeButton(
            modifier = actionModifier,
            mediaController = mediaController,
            enabled = enabled,
            track = track,
        )
    },
    trailingIcon = {
        MoreInfoButton {
            MoreInfoButtonContent(track,tracks,details,onRemove)
        }
    }
)

@Composable
private fun ColumnScope.MoreInfoButtonContent(
    track: Track,
    tracks : List<Track>,
    details: TrackDetails?,
    onRemove : ((Track) -> Unit)?
) {
    @Suppress("UNUSED_EXPRESSION") this
    val appState = LocalAppState.current
    val mediaController = appState.mediaController

    PlayNowDropDownItem(
        onClick = {
            onTrackItemClick(
                mediaController = mediaController,
                appState = appState,
                track = track,
                tracks = tracks,
            )
        }
    )

    PlayNexDropDownItem(
        onClick = {
            onTrackItemClick(
                mediaController = mediaController,
                appState = appState,
                track = track,
                tracks = tracks,
                addOnNext = true
            )
        }
    )

    val musicRepository = appState.musicRepository

    AddToQueueDropDownItem {
        it?.addMediaItem(track.asMediaItem(musicRepository))
    }

    TrackMetadataDropDownItem(track = track)

    ShareThumbItem()

    RingtoneSelectorDropDownItem(
        track = track,
        details = { details!! },
        enabled = details != null,
    )

    onRemove?.let {
        DeleteDropDownItem(
            label = stringResource(R.string.remove_track_from_collection),
            name = { details!!.name },
            enabled = details?.name != null,
            onDelete = { _ ->
                it(track)
            }
        )
    }

    val userRepository = LocalAppState.current.userRepository

    DeleteDropDownItem(
        label = stringResource(R.string.delete_track_from_device),
        name = { details!!.name },
        enabled = details?.name != null,
        onDelete = { coroutineScope ->
            coroutineScope.trackOnDelete(userRepository, track)
        }
    )
}
