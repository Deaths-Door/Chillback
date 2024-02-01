package com.deathsdoor.chillback.ui.providers

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.data.viewmodel.CoreViewModel

val LocalCoreViewModel = compositionLocalOf<CoreViewModel> { error("No Settings provided") }
val LocalSettings = compositionLocalOf<Settings> { error("No Settings provided") }

val LocalErrorSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for errors is not provided") }
val LocalSuccessSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for successes is not provided") }
val LocalInfoSnackbarState = compositionLocalOf<SnackbarHostState> { error("SnackbarState for information is not provided") }

@Composable
@NonRestartableComposable
fun InitializeProviders(content : @Composable () -> Unit) {
    val context = LocalContext.current

    val coreViewModel = viewModel { CoreViewModel(context = context) }

    val errorSnackbarHostState = remember { SnackbarHostState() }
    val successSnackbarHostState = remember { SnackbarHostState() }
    val infoSnackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalSettings provides coreViewModel.settings,
        LocalCoreViewModel provides coreViewModel,
        LocalErrorSnackbarState provides errorSnackbarHostState,
        LocalSuccessSnackbarState provides successSnackbarHostState,
        LocalInfoSnackbarState provides infoSnackbarHostState,
        content = content
    )
}