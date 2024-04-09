package com.deathsdoor.chillback.ui.providers

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deathsdoor.chillback.components.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.components.snackbar.rememberStackableSnackbarHostState
import com.deathsdoor.chillback.ui.state.ChillbackApplicationState

val LocalAppState = compositionLocalOf<ChillbackApplicationState> { error("No AppState Provided") }
val LocalWindowAdaptiveSize = compositionLocalOf<WindowSizeClass> { error("No WindowSize Provided") }

val LocalSnackbarState = compositionLocalOf<StackableSnackbarState> { error("No SnackbarState provided") }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@NonRestartableComposable
fun InitializeProviders(content : @Composable () -> Unit) {
    val windowAdaptiveInfo = calculateWindowSizeClass()
    val coroutineScope = rememberCoroutineScope()
    val stackedSnackbarHostState = rememberStackableSnackbarHostState(coroutineScope = coroutineScope)
    val appState = viewModel(ChillbackApplicationState::class)

    CompositionLocalProvider(
        LocalAppState provides appState,
        LocalWindowAdaptiveSize provides windowAdaptiveInfo,
        LocalSnackbarState provides stackedSnackbarHostState,
        content = content
    )
}