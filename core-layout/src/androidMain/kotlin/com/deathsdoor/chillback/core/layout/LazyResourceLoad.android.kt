package com.deathsdoor.chillback.core.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext

@NonRestartableComposable
@Composable
actual inline fun LazyResource(content: @Composable() (LazyResourceLoader.() -> Unit)) {
    val loader = LazyResourceLoader(LocalContext.current)
    content(loader)
}