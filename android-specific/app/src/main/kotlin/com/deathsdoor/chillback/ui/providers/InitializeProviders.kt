package com.deathsdoor.chillback.ui.providers

import StackedSnakbarHostState
import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.ui.state.ChillbackAppState
import rememberStackedSnackbarHostState

val LocalAppState = compositionLocalOf<ChillbackAppState> { error("No AppState Provided") }
val LocalWindowAdaptiveSize = compositionLocalOf<WindowSizeClass> { error("No WindowSize Provided") }

val LocalSnackbarState = compositionLocalOf<StackedSnakbarHostState> { error("No SnackbarState provided") }

@Deprecated("Do not use", ReplaceWith("com.deathsdoor.chillback.ui.providers.LocalSnackbarState"))
val LocalErrorSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for errors is not provided") }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@NonRestartableComposable
fun InitializeProviders(activity: Activity,content : @Composable () -> Unit) {
    val navController = rememberNavController()
    val windowAdaptiveInfo = calculateWindowSizeClass(activity = activity)
    val stackedSnackbarHostState = rememberStackedSnackbarHostState()

    val appState = viewModel {
        ChillbackAppState(
            application = activity.application,
            stackedSnackbarHostState = stackedSnackbarHostState,
            navController = navController,
        )
    }

    CompositionLocalProvider(
        LocalAppState provides appState,
        LocalWindowAdaptiveSize provides windowAdaptiveInfo,
        LocalSnackbarState provides stackedSnackbarHostState,
        content = content
    )
}

@Composable
@NonRestartableComposable
internal fun InitializeProvidersForPreview(content : @Composable () -> Unit) {
    val stackedSnackbarHostState = rememberStackedSnackbarHostState()

    CompositionLocalProvider(
        LocalWindowAdaptiveSize provides currentWindowAdaptiveInfo(),
        LocalSnackbarState provides stackedSnackbarHostState,
        content = content
    )
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun currentWindowAdaptiveInfo(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    return WindowSizeClass.calculateFromSize(size)
}