package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R

@Composable
fun ShowPlaybackQueueButton(
    modifier : Modifier = Modifier,
    onClick : () -> Unit
) = IconButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            painter = painterResource(R.drawable.ic_queue),
            contentDescription = "Open Playback Queue"
        )
    }
)