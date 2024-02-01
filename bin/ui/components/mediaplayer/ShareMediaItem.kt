package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem

@Composable
fun ShareMediaItem(
    modifier: Modifier = Modifier,
    mediaItem: MediaItem
) {
    IconButton(
        modifier = modifier,
        onClick = { /*TODO : Share current mediaItem*/ },
        content = {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Song"
            )
        }
    )
}