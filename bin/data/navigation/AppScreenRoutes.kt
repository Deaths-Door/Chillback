package com.deathsdoor.chillback.data.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

enum class AppScreenRoutes(override val route : String,val icon : ImageVector) : Route {
    EventMaps("event-maps", Icons.Default.Search) {
        @Composable
        override fun Content(navController: NavHostController, vararg other: Any?) = TODO("Not yet implemented")
    },
    Explore("explore", Icons.Default.Search){
        @Composable
        override fun Content(navController: NavHostController, vararg other: Any?) = TODO("Not yet implemented")
    },
    UserLibrary("user-lib", Icons.Default.Search){
        @Composable
        override fun Content(navController: NavHostController, vararg other: Any?) {
            // TODO("Not yet implemented , replace with actual UI")
            navController.navigate(SettingScreenRoutes.ROUTE)
        }
    };

    companion object {
        fun navGraph(navGraph: NavGraphBuilder, navController : NavHostController) = AppScreenRoutes.values().forEach { route ->
            navGraph.composable(route = route.route,content = { route.Content(navController,) })
        }
    }
}