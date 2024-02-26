package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
fun PlayPauseButton(modifier: Modifier = Modifier) {
    val mediaController = LocalAppState.current.mediaController
    val isShowing by remember(mediaController?.mediaItemCount) {
        mutableStateOf(mediaController != null && mediaController.mediaItemCount != 0)
    }

    if(isShowing) PlayPauseButton(
        modifier = modifier,
        mediaController = mediaController!!,
    )
}

@Composable
private fun PlayPauseButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    var isPlaying by remember { mutableStateOf(mediaController.isPlaying) }

    DisposableEffect(Unit) {
        val playerListener = object : Player.Listener {
            override fun onIsPlayingChanged(_isPlaying: Boolean) {
                super.onIsPlayingChanged(_isPlaying)
                isPlaying = _isPlaying
            }
        }

        mediaController.addListener(playerListener)

        onDispose {
            mediaController.removeListener(playerListener)
        }
    }

    IconButton(
        modifier = modifier.size(64.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { if(isPlaying) mediaController.pause() else mediaController.play() },
        content = {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.media3_notification_pause else R.drawable.media3_notification_play),
                contentDescription = "Current song is " + if (isPlaying) "playing" else "paused"
            )
        }
    )
}