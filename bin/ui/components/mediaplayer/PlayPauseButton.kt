package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    val isPlaying by remember(mediaController.isPlaying) { mutableStateOf(mediaController.isPlaying) }

    IconButton(
        modifier = modifier,
        onClick = { if(isPlaying) mediaController.play() else mediaController.pause() },
        content = {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.media3_notification_play else R.drawable.media3_notification_pause),
                contentDescription = "Current song is " + if (isPlaying) "playing" else "paused"
            )
        }
    )
}