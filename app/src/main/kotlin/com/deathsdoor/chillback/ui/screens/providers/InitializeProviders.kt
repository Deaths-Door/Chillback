package com.deathsdoor.chillback.ui.screens.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.data.viewmodel.CoreViewModel

val LocalSettings = compositionLocalOf<Settings> { error("No Settings provided") }
val LocalCoreViewModel = compositionLocalOf<CoreViewModel> { error("No Settings provided") }

@Composable
@NonRestartableComposable
fun InitializeProviders(content : @Composable () -> Unit) {
    val context = LocalContext.current
    val coreViewMutableList = viewModel { CoreViewModel(context) }
    val settings = Settings(LocalContext.current)
    CompositionLocalProvider(
        LocalSettings provides settings,
        LocalCoreViewModel provides coreViewMutableList,
        content = content
    )
}