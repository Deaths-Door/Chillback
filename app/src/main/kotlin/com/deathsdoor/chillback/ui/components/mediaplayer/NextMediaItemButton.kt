package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.Player

@Composable
fun NextMediaItemButton(
    modifier: Modifier = Modifier,
    mediaController: Player,
){
    val hasPrevious by remember(mediaController.hasPreviousMediaItem()) { mutableStateOf(mediaController.hasPreviousMediaItem()) }

    IconButton(
        modifier = modifier,
        onClick = { mediaController.seekToPreviousMediaItem() },
        enabled = hasPrevious
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "Seek Forward Button",
        )
    }
}