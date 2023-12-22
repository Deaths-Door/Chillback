package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.ui.screens.auth.AuthScreenGetStarted
import com.deathsdoor.chillback.ui.screens.auth.AuthScreenOption

private const val _ROUTE : String = "auth"

enum class AuthScreenRoutes(override val route: String) : Route {
    GetStarted("$_ROUTE/get-started") {
        @Composable
        override fun Content(navController: NavHostController) = AuthScreenGetStarted(navController)
    },
    AuthOption("$_ROUTE/auth-option") {
        @Composable
        override fun Content(navController: NavHostController) = AuthScreenOption(navController)
    };

    companion object {
        const val ROUTE = _ROUTE

        fun navGraph(navGraph: NavGraphBuilder,navController : NavHostController) = navGraph.navigation(
            startDestination = GetStarted.route,
            route = ROUTE,
            builder = {
                values().forEach { route ->
                    composable(route = route.route, content = { route.Content(navController) })
                }
            }
        )
    }
}