package com.deathsdoor.chillback.ui.screens.mediaplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import com.deathsdoor.chillback.ui.components.layout.BackButton
import com.deathsdoor.chillback.ui.providers.LocalCoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackQueueScreen(
    navController: NavController,
    mediaController: MediaController
) = Column {
    TopAppBar(
        navigationIcon = { BackButton { navController.popBackStack() } },
        title = { Text("Playback Queue") },
    )


}

private fun
