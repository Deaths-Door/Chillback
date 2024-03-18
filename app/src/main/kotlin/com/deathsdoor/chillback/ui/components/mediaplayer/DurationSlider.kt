package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.session.MediaController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationSlider(
    mediaController : MediaController,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled
        )
    },
    track: @Composable (SliderState) -> Unit = { sliderState ->
        SliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            sliderState = sliderState
        )
    },
){
    val duration by rememberMediaItemDuration(mediaController)

    val currentPosition by remember(mediaController.currentPosition) { mutableStateOf(mediaController.currentPosition.toFloat()) }
    var newPosition by remember { mutableStateOf(0f) }

    Slider(
        modifier = modifier,
        value = currentPosition,
        colors = colors,
        enabled = enabled,
        interactionSource = interactionSource,
        thumb = thumb,
        track = track,
        valueRange = 0f..duration.toFloat(),
        onValueChange = { newPosition = it },
        onValueChangeFinished = { mediaController.seekTo(newPosition.toLong()) },
    )
}

@Composable
fun rememberMediaItemDuration(mediaController: Player) = remember(mediaController.duration) {
    val mediaDuration = mediaController.duration

    // This should not be needed , but is there for safely
    // Since mediaController.duration defaults to C.TIME_UNSET when no duration given , which is Long.MIN_VALUE + 1
    val durationAfterCorrection = if(mediaDuration == C.TIME_UNSET) 1 else mediaDuration
    mutableStateOf(durationAfterCorrection)
}