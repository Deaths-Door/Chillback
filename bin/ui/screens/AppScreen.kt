package com.deathsdoor.chillback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.data.navigation.AppScreenRoutes
import com.deathsdoor.chillback.data.navigation.AuthScreenRoutes
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.ui.components.layout.ErrorSnackbar
import com.deathsdoor.chillback.ui.components.layout.InfoSnackbar
import com.deathsdoor.chillback.ui.components.layout.SuccessSnackbar
import com.deathsdoor.chillback.ui.components.mediaplayer.MediaPlayer
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalInfoSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalSuccessSnackbarState

@Composable
fun AppScreen(showLoginScreen : Boolean) {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = { CreateSnackbars() },
        bottomBar = { CreateCoreNavigationBar(navController) },
        content = {
            Box(
                modifier = Modifier.fillMaxSize().padding(it)) {
                CreateNavigationHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = if(showLoginScreen) AuthScreenRoutes.ROUTE else AppScreenRoutes.UserLibrary.route
                )

                MediaPlayer(modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    )
}

@Composable
@NonRestartableComposable
private fun CreateNavigationHost(
    modifier : Modifier = Modifier,
    navController : NavHostController,
    startDestination : String,
) = NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination,
    builder = {
        // TODO: Add more routes when it is needed
        AppScreenRoutes.navGraph(navGraph = this,navController = navController)
        AuthScreenRoutes.navGraph(navGraph = this,navController = navController)
        SettingScreenRoutes.navGraph(navGraph = this,navController = navController)
    }
)


@Composable
private fun CreateCoreNavigationBar(navController: NavHostController) = NavigationBar {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    AppScreenRoutes.values().forEach {
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.route == it.route,
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

@Composable
private fun CreateSnackbars() = mapOf<SnackbarHostState,@Composable  (SnackbarData) -> Unit>(
    LocalErrorSnackbarState.current to { ErrorSnackbar(it)},
    LocalSuccessSnackbarState.current to { SuccessSnackbar(it) },
    LocalInfoSnackbarState.current to { InfoSnackbar(it) }
).forEach { (state,action) ->
    SnackbarHost(
        hostState = state,
        modifier = Modifier.padding(8.dp),
        snackbar = action
    )
}