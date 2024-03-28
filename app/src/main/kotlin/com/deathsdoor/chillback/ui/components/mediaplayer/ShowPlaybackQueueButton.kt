package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R

@Composable
@NonRestartableComposable
fun ShowPlaybackQueueButton(modifier : Modifier = Modifier) = IconButton(
    modifier = modifier,
    onClick = { TODO("Navigate to playbackqueue list screen") },
    content = {
        Icon(
            painter = painterResource(R.drawable.playback_queue),
            contentDescription = "Open Playback Queue"
        )
    }
)