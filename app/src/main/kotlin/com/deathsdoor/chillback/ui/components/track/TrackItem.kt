package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnail
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnailCard
import com.deathsdoor.chillback.ui.components.layout.ThumbnailCaption
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.components.layout.applyToggleableOnSelection
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.dragselectcompose.core.DragSelectState

@Composable
@NonRestartableComposable
fun TrackCard(
    modifier : Modifier = Modifier,
    mediaController: Player?,
    track: Track,
    details : TrackDetails?,
    isSelected : Boolean?,
    draggableState : DragSelectState<Track>,
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
                    // TODO : Show more info for track
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
    details : TrackDetails?,
    isSelected : Boolean?,
    draggableState : DragSelectState<Track>,
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
            // TODO : Show more info for track
        }
    }
)