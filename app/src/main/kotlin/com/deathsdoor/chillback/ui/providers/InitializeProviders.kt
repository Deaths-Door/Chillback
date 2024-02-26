package com.deathsdoor.chillback.ui.providers

import android.app.Application
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.ui.state.ChillbackAppState

val LocalAppState = compositionLocalOf<ChillbackAppState> { error("No AppState Provided") }

val LocalErrorSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for errors is not provided") }
val LocalWarningSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for warnings is not provided") }

@Composable
@NonRestartableComposable
fun InitializeProviders(application : Application,content : @Composable () -> Unit) {
    val navController = rememberNavController()
    val appState = viewModel {
        ChillbackAppState(
            application = application,
            navController = navController
        )
    }

    val errorSnackbarHostState = remember { SnackbarHostState() }
    val warningSnackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalAppState provides appState,
        LocalErrorSnackbarState provides errorSnackbarHostState,
        LocalWarningSnackbarState provides warningSnackbarHostState,
        content = content
    )
}