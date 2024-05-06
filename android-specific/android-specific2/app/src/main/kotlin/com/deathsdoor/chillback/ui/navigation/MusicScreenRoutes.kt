package com.deathsdoor.chillback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.ui.components.collection.TrackCollectionScreen
import com.deathsdoor.chillback.ui.screens.playbackqueue.PlaybackQueueScreen
import com.deathsdoor.chillback.ui.state.ChillbackAppState

private const val BASE = "music"
private const val MainRoute = "collection-screen"
private const val PLAYBACK_QUEUE = "collection-screen"

fun ChillbackAppState.navigateToTrackCollectionScreen(repository: TrackCollectionRepository) {
    trackCollectionRepository = repository
    navController.navigate(BASE)
}

fun NavController.navigateToPlaybackScreen() {
    navigate(BASE)
    navigate(PLAYBACK_QUEUE)
}

fun NavGraphBuilder.addMusicScreenRoutes(appState: ChillbackAppState) = navigation(route = BASE, startDestination = MainRoute) {
    composable(
        route = MainRoute,
        exitTransition = {
            appState.trackCollectionRepository = null
            null
        },
        content = { TrackCollectionScreen(repository = appState.trackCollectionRepository!!) }
    )

    composable(PLAYBACK_QUEUE) { PlaybackQueueScreen() }
}