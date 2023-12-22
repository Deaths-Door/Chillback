package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

sealed interface Route {
    val route : String

    @Composable
    fun Content(navController: NavHostController)
}