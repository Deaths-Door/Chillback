package com.deathsdoor.chillback.ui.screens.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.deathsdoor.chillback.data.settings.Settings

val LocalSettings = compositionLocalOf<Settings> { error("No Settings provided") }

@Composable
@NonRestartableComposable
fun InitializeProviders(content : @Composable () -> Unit) {
    val settings = Settings(LocalContext.current)
    CompositionLocalProvider(
        LocalSettings provides settings,
        content = content
    )
}