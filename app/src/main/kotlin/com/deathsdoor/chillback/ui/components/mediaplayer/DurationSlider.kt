package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.session.MediaController

@Composable
fun DurationSlider(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
    colors: SliderColors = SliderDefaults.colors()
){
    var newPosition by remember { mutableLongStateOf(0L) }

    // TODO : Find a better fix to this
    Slider(
        modifier = modifier,
        value = 1f,//mediaController.currentPosition.toFloat(),
        colors = colors,
        valueRange = 0f..100f,//mediaController.duration.toFloat(),
        onValueChange = {
            newPosition = it.toLong()
        },
        onValueChangeFinished = {
            mediaController.seekTo(newPosition)
        },
    )
}