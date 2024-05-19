package com.deathsdoor.chillback.feature.mediaplayer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.feature.mediaplayer.components.EqualizerScreen

fun NavGraphBuilder.setUpMediaPlayerRoutes(navController : NavController) = navigation(route = "mediaplayer", startDestination = "equalizer") {
    composable("equalizer") {
        EqualizerScreen(navController = navController)
    }
}

private fun NavController.navigateToBase() = navigate("mediaplayer")

internal fun NavController.navigateToEqualizerScreen() {
    navigateToBase()
    navigate("equalizer")
}