package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.session.MediaController

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    val isPlaying by remember(mediaController?.isPlaying) { mutableStateOf(mediaController.isPlaying) }

    IconButton(
        modifier = modifier,
        onClick = { if(isPlaying) mediaController.play() else mediaController.pause() },
        content = {
            Icon(
                imageVector = if (isPlaying) Icons.Filled.PlayArrow else Icons.Filled.KeyboardArrowUp,
                contentDescription = "Current song is " + if (isPlaying) "playing" else "paused"
            )
        }
    )
}