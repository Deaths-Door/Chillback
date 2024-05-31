package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.astroplayer.ui.PauseButton
import com.deathsdoor.astroplayer.ui.PlayButton
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.components.VerticalMediaItemTitleWithArtists
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource

private val ARROW_HEIGHT = 16.dp
private val ARROW_WIDTH = ARROW_HEIGHT * 3

@Composable
internal fun CollapsedMediaPlayer(
    modifier : Modifier = Modifier,
    state : AstroPlayerState,
    onClick : () -> Unit
) =  Column(modifier = modifier) {
    Card(
        modifier = Modifier
            .align(Alignment.End)
            .size(height = ARROW_HEIGHT, width = ARROW_WIDTH)
            .padding(end = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(Res.strings.expand_mediaplayer)
            )
        }
    )

    Card {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                val iconSizeModifier = Modifier.size(52.dp)

                TrackArtwork(
                    modifier = iconSizeModifier,
                    mediaItem = state.currentMediaItem!!
                )

                VerticalMediaItemTitleWithArtists(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    mediaItem = state.currentMediaItem!!,
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    artistStyle = LocalTextStyle.current
                )

                LikeButton(
                    modifier = iconSizeModifier,
                    astroPlayer = state.astroPlayer,
                    mediaItem = state.currentMediaItem!!
                )

                RepeatMediaItemsButton(
                    modifier = iconSizeModifier,
                    state = state
                )

                Spacer(modifier = Modifier.width(8.dp))

                when(state.isPlaying) {
                    true -> PlayButton(
                        modifier = iconSizeModifier,
                        onClick = {
                            state.astroPlayer.play()
                        }
                    )
                    else -> PauseButton(
                        modifier = iconSizeModifier,
                        onClick = {
                            state.astroPlayer.pause()
                        }
                    )
                }
            }
        )

        DurationSlider(
            modifier = Modifier.heightIn(max = 8.dp),
            state = state
        )
    }
}