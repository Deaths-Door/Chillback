package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.ui.AstroPlayerState

@Composable
internal fun ExpandedFullScreenMediaPlayer(
    modifier : Modifier = Modifier,
    state : AstroPlayerState,
    header : @Composable (Modifier) -> Unit,
    action : (@Composable RowScope.() -> Unit)? = null,
) = Box(modifier) {
    TrackArtwork(
        modifier = Modifier.fillMaxSize(),
        mediaItem = state.currentMediaItem!!,
        contentScale = ContentScale.FillBounds
    )

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp,vertical = 24.dp)) {
        val maxWidth = Modifier.fillMaxWidth()

        header(maxWidth)

        AnimatedMediaItemCover(mediaItem = state.currentMediaItem!!)

        HorizontalDurationMarkers(
            modifier = maxWidth,
            state = state,
            content = {
                DurationSlider(
                    modifier = Modifier.weight(1f),
                    state = state,
                )
            }
        )

        val iconSize =  72.dp

        Row(modifier = maxWidth) {
            Spacer(modifier = Modifier.weight(2f))

            MainMediaActionRow(
                modifier = maxWidth,
                state = state,
                iconSize = iconSize
            )

            Spacer(modifier = Modifier.weight(1f))

            SecondaryMediaActionRow(
                state = state,
                iconSize =  iconSize,
            )

            action?.invoke(this)
        }
    }
}