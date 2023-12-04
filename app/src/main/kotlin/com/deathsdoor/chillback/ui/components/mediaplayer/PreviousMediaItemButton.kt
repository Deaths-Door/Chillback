package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.media3.session.MediaController

@Composable
fun PreviousMediaItemButton(
    modifier: Modifier = Modifier,
    mediaController: MediaController,
){
    val hasNext by remember(mediaController.hasNextMediaItem()) { mutableStateOf(mediaController.hasNextMediaItem()) }

    IconButton(
        modifier = modifier,
        onClick = { mediaController.seekToNextMediaItem() },
        enabled = hasNext
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "Seek Backwards Button",
        )
    }
}