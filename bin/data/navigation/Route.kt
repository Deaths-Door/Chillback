package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.navigation.NavHostController

sealed interface Route {
    val route : String

    @Composable
    @NonRestartableComposable
    fun Content(navController: NavHostController,vararg other : Any?)
}