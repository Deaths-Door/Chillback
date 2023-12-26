package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.providers.LocalCoreViewModel
import com.deathsdoor.chillback.ui.providers.LocalSettings

@Composable
fun ShuffleButton(
    modifier : Modifier = Modifier,
    mediaController: MediaController
) {
    val isShuffled by remember(mediaController.shuffleModeEnabled) { mutableStateOf(mediaController.shuffleModeEnabled) }

    IconToggleButton(
        modifier = modifier,
        checked = isShuffled,
        onCheckedChange = { mediaController.shuffleModeEnabled = it },
        content = {
            Image(
                imageVector = Icons.Default.Lock,
                contentDescription = "Shuffle Songs"
            )
        }
    )
}