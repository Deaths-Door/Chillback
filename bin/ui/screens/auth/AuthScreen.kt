package com.deathsdoor.chillback.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.data.navigation.AuthScreenRoutes

@Composable
fun AuthScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthScreenRoutes.values().first().route,
        builder = {
            AuthScreenRoutes.values().forEach { route ->
                composable(route = route.route,content = { route.content(navController) })
            }
        }
    )
}
