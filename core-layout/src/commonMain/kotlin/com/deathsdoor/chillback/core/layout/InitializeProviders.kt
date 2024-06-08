package com.deathsdoor.chillback.core.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackedSnackbarHost
import com.deathsdoor.chillback.core.layout.snackbar.rememberStackableSnackbarHostState

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun InitializeProviders(content : @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val hostState = rememberStackableSnackbarHostState(coroutineScope = coroutineScope, maxStack = 5)
    CompositionLocalProvider(
        LocalWindowSize provides calculateWindowSizeClass(),
        LocalSnackbarState provides hostState,
        content = {
            Box {
                content()
                StackedSnackbarHost(modifier = Modifier.align(Alignment.BottomCenter),hostState = hostState)
            }
        }
    )
}


val LocalWindowSize = compositionLocalOf<WindowSizeClass> { error("No Window Size Class Provided") }
val LocalSnackbarState = compositionLocalOf<StackableSnackbarState> { error("No Snackbar State  Provided") }