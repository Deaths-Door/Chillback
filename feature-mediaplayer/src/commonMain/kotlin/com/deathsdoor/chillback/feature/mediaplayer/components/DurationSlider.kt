package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.addListener
import com.deathsdoor.astroplayer.core.listeners.AstroListener
import com.deathsdoor.astroplayer.ui.AstroPlayerState

@Composable
internal fun DurationSlider(
    state : AstroPlayerState,
    modifier: Modifier = Modifier
) {
    var newPosition by remember { mutableFloatStateOf(0f) }

    Slider(
        modifier = modifier,
        value = state.astroPlayer.currentPosition.toFloat(),
        valueRange = 0f..state.astroPlayer.contentDuration.toFloat(),
        onValueChange = { newPosition = it },
        onValueChangeFinished = { state.astroPlayer.seekTo(newPosition.toLong()) },
    )
}