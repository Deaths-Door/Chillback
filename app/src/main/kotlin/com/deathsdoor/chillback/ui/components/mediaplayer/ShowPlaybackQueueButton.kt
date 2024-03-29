package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.navigation.navigateToPlaybackScreen
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
@NonRestartableComposable
fun ShowPlaybackQueueButton(modifier : Modifier = Modifier) {
    val navController = LocalAppState.current.navController
    IconButton(
        modifier = modifier,
        onClick = { navController.navigateToPlaybackScreen() },
        content = {
            Icon(
                painter = painterResource(R.drawable.playback_queue),
                contentDescription = "Open Playback Queue"
            )
        }
    )
}