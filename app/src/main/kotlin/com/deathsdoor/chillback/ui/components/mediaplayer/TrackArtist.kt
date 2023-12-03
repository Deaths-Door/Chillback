package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// TODO : Load artwork with real logic
@Composable
fun TrackArtwork(modifier : Modifier = Modifier,) = Image(
    modifier = modifier,
    imageVector = Icons.Default.Info,
    contentDescription = null,
)