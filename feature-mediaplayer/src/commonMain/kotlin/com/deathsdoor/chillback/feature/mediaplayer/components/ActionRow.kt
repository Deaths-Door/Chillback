package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.astroplayer.ui.NextMediaItemButton
import com.deathsdoor.astroplayer.ui.PauseButton
import com.deathsdoor.astroplayer.ui.PlayButton
import com.deathsdoor.astroplayer.ui.PreviousMediaItemButton

@Composable
internal fun MainMediaActionRow(
    state : AstroPlayerState,
    iconSize : Dp = 24.dp,
    modifier: Modifier = Modifier
) =  Row(modifier = modifier,horizontalArrangement = Arrangement.Center) {
    val iconSizeModifier = Modifier.size(iconSize)

    ShuffleButton(
        modifier = iconSizeModifier,
        state = state,
    )

    PreviousMediaItemButton(
        modifier = iconSizeModifier,
        astroPlayerState = state,
    )

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

    NextMediaItemButton(
        modifier = iconSizeModifier,
        astroPlayerState = state,
    )

    RepeatMediaItemsButton(
        modifier = iconSizeModifier,
        state = state,
    )
}

@Suppress("UnusedReceiverParameter")
@Composable
internal fun RowScope.SecondaryMediaActionRow(
    state : AstroPlayerState,
    navController: NavController,
    iconSize : Dp = 24.dp,
) {
    val iconSizeModifier = Modifier.size(iconSize)

    LyricsButton(
        modifier = iconSizeModifier,
        navController = navController
    )

    EqualizerButton(
        modifier = iconSizeModifier,
        navController = navController
    )

    // TODO -> + Show playback queue

    ShareMediaItemButton(
        modifier = iconSizeModifier,
        mediaItem = state.currentMediaItem!!
    )
}