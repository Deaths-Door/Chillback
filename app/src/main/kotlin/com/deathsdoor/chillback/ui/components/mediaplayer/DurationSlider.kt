package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.mediaplayer.rememberMediaItemDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationSlider(
    mediaController : MediaController,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderPositions) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = DpSize.Zero.copy(15.0.dp,15.0.dp)
        )
    },
    track: @Composable (SliderPositions) -> Unit = { sliderPositions ->
        SliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            sliderPositions = sliderPositions
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