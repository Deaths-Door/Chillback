package com.deathsdoor.chillback.ui.screens.mediaplayer

import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.data.navigation.ExpandedMediaPlayerRoutes
import com.deathsdoor.chillback.ui.components.layout.modalsheet.ModalSheetState

@Composable
fun ExpandedMediaPlayer(
    sheetState: ModalSheetState,
    mediaController : MediaController,
    currentMediaItem : MediaItem
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ExpandedMediaPlayerRoutes.Default.route,
        builder = {
            ExpandedMediaPlayerRoutes.navGraph(
                navGraph = this,
                navController = navController,
                sheetState = sheetState,
                mediaController = mediaController,
                currentMediaItem = currentMediaItem
            )
        }
    )
}


