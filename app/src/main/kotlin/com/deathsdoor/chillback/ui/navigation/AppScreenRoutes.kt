package com.deathsdoor.chillback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.screens.app.LocalSongsLibraryScreen
import com.deathsdoor.chillback.ui.screens.app.UserLibrary
import com.deathsdoor.chillback.ui.state.ChillbackAppState

private const val UserLibraryRoute = "library"
private const val ConcertMapsRoute = "concert-maps"
private const val Explore = "explore"
const val LocalSongsLibrary = "localsongslibrary"
const val appScreenInitialRoute = "appRoutes"

fun NavGraphBuilder.addAppScreenRoutes() {
    navigation(route = appScreenInitialRoute,startDestination = UserLibraryRoute) {
        composable(UserLibraryRoute) { UserLibrary() }
        composable(LocalSongsLibrary) { LocalSongsLibraryScreen() }
    }
    composable(Explore) { TODO() }
    composable(ConcertMapsRoute) { TODO() }
}

fun forEachCoreAppScreenRoute(block : (route : String, label : String, id : Int) -> Unit) {
    block(ConcertMapsRoute,"Concerts",R.drawable.concert)
    block(Explore,"Explore",R.drawable.explore)
    block(UserLibraryRoute,"Library",R.drawable.lib)
}

fun NavController.navigateToApp() = navigate(appScreenInitialRoute)

fun ChillbackAppState.navigateToFavoritesScreen() = navigateToTrackCollectionScreen(repository = userRepository.favouriteTracks)

fun ChillbackAppState.navigateToTopPlayedScreen() = navigateToTrackCollectionScreen(repository = userRepository.topPlayedTracks)

fun ChillbackAppState.navigateToLocalSongsLibraryScreen() = navController.navigate(LocalSongsLibrary)