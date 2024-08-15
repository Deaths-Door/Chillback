package com.deathsdoor.chillback.core.media.components.mediaitem

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.ThumbnailCaption
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.layout.actions.MoreInfoButton
import com.deathsdoor.chillback.core.media.components.DraggableHandle
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.core.media.components.SelectableThumbnailCard
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
internal fun ReorderableCollectionItemScope.MediaItemCard(
    modifier : Modifier = Modifier,
    astroPlayer : AstroPlayer,
    mediaItem : AstroMediaItem,
    isSelected : Boolean?,
    elevation : Dp,
    interactionSource : MutableInteractionSource,
) {
    val textModifier = Modifier.padding(start = 16.dp)

    SelectableThumbnailCard(
        modifier = modifier,
        isSelected = isSelected,
        elevation = elevation,
        artwork = { TrackArtwork(mediaItem = mediaItem) },
        title = {
            ThumbnailTitle(
                modifier = textModifier,
                text = mediaItem.metadata?.displayTitle ?: stringResource(Res.strings.wait_a_second),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        caption = mediaItem.metadata?.artist?.let {{ ThumbnailCaption(modifier = textModifier,text = it) } },
        actionIcon = { actionModifier , enabled ->
            DraggableHandle(
                modifier = Modifier.align(Alignment.BottomStart)
                    .padding(top = 12.dp,start = 12.dp),
                interactionSource = interactionSource
            )

            LikeButton(
                modifier = actionModifier,
                astroPlayer = astroPlayer,
                mediaItem = mediaItem,
                enabled = enabled,
            )

            MediaItemMoreInfoButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
            )
        }
    )
}