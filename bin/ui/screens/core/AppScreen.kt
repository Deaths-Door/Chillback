package com.deathsdoor.chillback.ui.screens.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.data.navigation.AppScreenRoutes
import com.deathsdoor.chillback.ui.components.mediaplayer.CollapsedMediaPlayer

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                CollapsedMediaPlayer()
                // TODO : Add Option to show/hide  nav bar
                CreateCoreNavigationBar(navController)
            }
        },
        content = { CreateCoreNavHost(navController,it) }
    )
}

@Composable
private fun CreateCoreNavHost(navController : NavHostController,paddingValues: PaddingValues) = NavHost(
    modifier = Modifier.padding(paddingValues),
    navController = navController,
    startDestination = AppScreenRoutes.UserLibrary.route,
    builder = {
        AppScreenRoutes.values().forEach { route ->
            composable(route = route.route,content = { route.content(navController) })
        }
    }
)

@Composable
private fun CreateCoreNavigationBar(navController: NavHostController) = NavigationBar {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AppScreenRoutes.values().forEach {
        NavigationBarItem(
            selected = currentDestination?.route == it.route,
            alwaysShowLabel = false,
            icon = { Icon(imageVector = it.icon, contentDescription = null) },
            // TODO : Do not use it.name , instead use 'id' for localization support
            label = { Text(it.name) },
            onClick = {
                navController.navigate(it.route) {
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            },
        )
    }
}