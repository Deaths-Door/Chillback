package com.deathsdoor.chillback.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.data.navigation.AppScreenRoutes
import com.deathsdoor.chillback.data.navigation.AuthScreenRoutes
import com.deathsdoor.chillback.data.navigation.Route
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.ui.components.layout.ErrorSnackbar
import com.deathsdoor.chillback.ui.components.layout.InfoSnackbar
import com.deathsdoor.chillback.ui.components.layout.SuccessSnackbar
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalInfoSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalSuccessSnackbarState


@Composable
fun AppScreen(showLoginScreen : Boolean) {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = { CreateSnackbars() },
        bottomBar = {
                    // TODO DO THIS
           // Column {
                //CollapsedMediaPlayer()
                // TODO : Add Option to show/hide  nav bar
                //CreateCoreNavigationBar(navController)
           // }
        },
        content = {
            CreateNavigationHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = if(showLoginScreen) AuthScreenRoutes.ROUTE else AppScreenRoutes.UserLibrary.route
            )
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
private fun CreateSnackbars() = mapOf<SnackbarHostState,@Composable  (string : String) -> Unit>(
    LocalErrorSnackbarState.current to { ErrorSnackbar(it)},
    LocalSuccessSnackbarState.current to { SuccessSnackbar(it) },
    LocalInfoSnackbarState.current to { InfoSnackbar(it) }
).forEach { (state,action) ->
    SnackbarHost(
        hostState = state,
        modifier = Modifier.padding(8.dp),
        snackbar = {
            action(it.visuals.message)
        }
    )
}