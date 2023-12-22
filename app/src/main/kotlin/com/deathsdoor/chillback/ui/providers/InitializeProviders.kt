package com.deathsdoor.chillback.ui.providers

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.deathsdoor.chillback.data.settings.Settings

val LocalSettings = compositionLocalOf<Settings> { error("No Settings provided") }

val LocalErrorSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for errors is not provided") }
val LocalSuccessSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for successes is not provided") }
val LocalInfoSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for information is not provided") }

@Composable
@NonRestartableComposable
fun InitializeProviders(content : @Composable () -> Unit) {
    val settings = Settings(LocalContext.current)

    val errorSnackbarHostState = remember { SnackbarHostState() }
    val successSnackbarHostState = remember { SnackbarHostState() }
    val infoSnackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalSettings provides settings,
        LocalErrorSnackbarState provides errorSnackbarHostState,
        LocalSuccessSnackbarState provides successSnackbarHostState,
        LocalInfoSnackbarState provides infoSnackbarHostState,
        content = content
    )
}