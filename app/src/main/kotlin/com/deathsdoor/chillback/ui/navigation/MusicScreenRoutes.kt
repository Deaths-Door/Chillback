package com.deathsdoor.chillback.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.ui.components.collection.TrackCollectionScreen
import com.deathsdoor.chillback.ui.state.ChillbackAppState

private const val MainRoute = "collection-screen"

fun ChillbackAppState.navigateToTrackCollectionScreen(repository: TrackCollectionRepository) {
    trackCollectionRepository = repository
    navController.navigate(MainRoute)
}

fun NavGraphBuilder.addMusicScreenRoutes(appState: ChillbackAppState) = navigation(route = "music", startDestination = MainRoute) {
    composable(
        route = MainRoute,
        exitTransition = {
            appState.trackCollectionRepository = null
            null
        },
        content = { TrackCollectionScreen(repository = appState.trackCollectionRepository!!) }
    )
}