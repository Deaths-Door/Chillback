package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.session.MediaController

@Composable
fun RepeatMediaItemsButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    var repeatMode by remember(mediaController.repeatMode) { mutableIntStateOf(mediaController.repeatMode)  }

    IconButton(
        modifier = modifier,
        onClick = { repeatMode = (repeatMode + 1) % 3 },
        content = {
            Icon(
                imageVector =  Icons.Filled.PlayArrow,
                // TODO : Change icon and repeat mode
                contentDescription = null
            )
        }
    )
}