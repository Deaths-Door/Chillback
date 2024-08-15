package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.feature.mediaplayer.icons.Shuffle
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

@Composable
@NonRestartableComposable
internal fun ShuffleButton(
    state : AstroPlayerState,
    modifier : Modifier = Modifier,
) = IconToggleButton(
    modifier = modifier,
    checked = state.shuffleModeEnabled,
    onCheckedChange = { state.astroPlayer.shuffleModeEnabled = it },
    content = {
        Icon(
            imageVector = Icons.Shuffle,
            contentDescription = stringResource(
                if(state.shuffleModeEnabled) Res.strings.disable_shuffle_mode
                else Res.strings.enable_shuffle_mode
            )
        )
    }
)