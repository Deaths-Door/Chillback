package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.deathsdoor.chillback.ui.components.layout.modalsheet.ModalSheetState
import com.deathsdoor.chillback.ui.screens.mediaplayer.ExpandedMediaPlayerInnerScreen
import com.deathsdoor.chillback.ui.screens.mediaplayer.PlaybackQueueScreen

enum class ExpandedMediaPlayerRoutes(override val route: String) : Route {
    Default("default") {
        @Composable
        override fun Content(navController: NavHostController, vararg other: Any?) = ExpandedMediaPlayerInnerScreen(
            navController = navController,
            sheetState = other[0] as ModalSheetState,
            mediaController = other[1] as MediaController,
            currentMediaItem = other[2] as MediaItem
        )
    },
    PlaybackQueue("playback-queue"){
        @Composable
        override fun Content(navController: NavHostController, vararg other: Any?) = PlaybackQueueScreen(
            navController = navController,
            mediaController = other[1] as MediaController,
        )
    };
    companion object {
        fun navGraph(
            navGraph: NavGraphBuilder,
            navController : NavHostController,
            sheetState: ModalSheetState,
            mediaController : MediaController,
            currentMediaItem : MediaItem
        ) = navGraph.apply {
            with(Default) {
                composable(route) {
                    Content(
                        navController,
                        sheetState,
                        mediaController,
                        currentMediaItem
                    )
                }
            }

            with(PlaybackQueue) {
                composable(route) {
                    Content(
                        navController,
                        mediaController,
                    )
                }
            }
        }
    }
}
