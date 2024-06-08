package com.deathsdoor.chillback.core.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

@NonRestartableComposable
@Composable
actual inline fun LazyResource(content: @Composable() (LazyResourceLoader.() -> Unit)) {
    val loader = LazyResourceLoader(Unit)
    content(loader)
}