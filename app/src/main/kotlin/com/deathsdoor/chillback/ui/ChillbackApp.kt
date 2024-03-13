package com.deathsdoor.chillback.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.deathsdoor.chillback.ui.components.mediaplayer.screen.MusicPlayer
import com.deathsdoor.chillback.ui.components.snackbars.CreateSnackbarHosts
import com.deathsdoor.chillback.ui.navigation.ForEachCoreAppScreenRoute
import com.deathsdoor.chillback.ui.navigation.LocalSongsLibrary
import com.deathsdoor.chillback.ui.navigation.TRACK_EXTRA_OPTIONS_METADATA_ROUTE
import com.deathsdoor.chillback.ui.navigation.addAppScreenRoutes
import com.deathsdoor.chillback.ui.navigation.addMusicScreenRoutes
import com.deathsdoor.chillback.ui.navigation.addTrackExtraOptionsRoutes
import com.deathsdoor.chillback.ui.navigation.appScreenInitialRoute
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
fun ChillbackApp() {
    val appState = LocalAppState.current
    val navController = appState.navController

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        snackbarHost = { CreateSnackbarHosts() },
        bottomBar = {
            AnimatedVisibility(currentRoute != LocalSongsLibrary && currentRoute != TRACK_EXTRA_OPTIONS_METADATA_ROUTE) {
                NavigationBar {
                    ChillbackAppNavigationBarContents(
                        navController =  navController,
                        navBackStackEntry = navBackStackEntry
                    )
                }
            }
        },
        content = {
            Box(modifier = Modifier.padding(it).fillMaxSize()) {
                NavHost(
                    modifier = Modifier.matchParentSize(),
                    navController = navController,
                    startDestination = appScreenInitialRoute,
                    builder = {
                        addAppScreenRoutes()
                        addMusicScreenRoutes(appState = appState)
                        addTrackExtraOptionsRoutes()

                    }
                )

                MusicPlayer(modifier = Modifier
                    // Because of NavigationBarTokens.ContainerHeight = 80.dp
                    .padding(bottom = if(currentRoute != LocalSongsLibrary) 0.dp else 80.dp)
                    .align(Alignment.BottomCenter)
                )
            }
        }
    )
}


@Composable
private fun RowScope.ChillbackAppNavigationBarContents(navController: NavController,navBackStackEntry: NavBackStackEntry?) {
    ForEachCoreAppScreenRoute { route, label, id ->
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.route == route,
            alwaysShowLabel = false,
            icon = { Icon(painter = painterResource(id = id), contentDescription = null) },
            label = { Text(label) },
            onClick = {
                navController.navigate(route) {
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