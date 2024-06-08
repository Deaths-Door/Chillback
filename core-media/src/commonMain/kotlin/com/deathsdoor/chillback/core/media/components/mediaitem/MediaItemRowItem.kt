package com.deathsdoor.chillback.core.media.components.mediaitem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.AdaptiveLayoutGeneric
import com.deathsdoor.chillback.core.layout.ThumbnailCaption
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.core.media.components.SelectableThumbnail
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.repositories.duration
import com.deathsdoor.chillback.core.media.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import sh.calvin.reorderable.ReorderableCollectionItemScope
import kotlin.time.Duration.Companion.minutes

// TODO : Add DraggableHandle for MediaItemRowItem
@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReorderableCollectionItemScope.MediaItemRowItem(
    modifier : Modifier = Modifier,
    astroPlayer : AstroPlayer,
    mediaItem : AstroMediaItem,
    isSelected : Boolean?,
    elevation : Dp,
    @Suppress("UNUSED_PARAMETER")
    interactionSource : MutableInteractionSource,
) = SelectableThumbnail(
    modifier = modifier,
    isSelected = isSelected,
    elevation = elevation,
    title = {
        ThumbnailTitle(
            text = mediaItem.metadata?.displayTitle ?: stringResource(Res.strings.wait_a_second),
            style = MaterialTheme.typography.headlineMedium
        )
    },
    caption = mediaItem.metadata?.artist?.let {{ ThumbnailCaption(text = it) } },
    artwork = { size -> TrackArtwork(modifier = Modifier.size(size),mediaItem = mediaItem) },
    actionIcon = AdaptiveLayoutGeneric<@Composable RowScope.(Modifier, enabled : Boolean) -> Unit>(
        onMobile = {{  actionModifier , enabled ->
            LikeButton(
                modifier = actionModifier,
                astroPlayer = astroPlayer,
                mediaItem = mediaItem,
                enabled = enabled,
            )
        }},
        onDesktop = {{  actionModifier , enabled ->
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.surfaceVariant) {
                Spacer(modifier = Modifier.weight(0.25f))

                Text(
                    modifier = Modifier.basicMarquee(),
                    text = mediaItem.metadata?.albumTitle ?: stringResource(Res.strings.wait_a_second)
                )

                Spacer(modifier = Modifier.weight(0.20f))

                Text(
                    modifier = Modifier.basicMarquee(),
                    text = mediaItem.metadata?.genre ?: stringResource(Res.strings.wait_a_second)
                )

                Spacer(modifier = Modifier.weight(0.75f))

                LikeButton(
                    modifier = actionModifier.padding(end = 16.dp),
                    astroPlayer = astroPlayer,
                    mediaItem = mediaItem,
                    enabled = enabled,
                )

                mediaItem.metadata?.duration?.let {
                    Text(text =  String.format("%02d:%02d", it.minutes, it))
                }
            }
        }}
    ),
    trailingIcon = {
        MediaItemMoreInfoButton()
    }
)