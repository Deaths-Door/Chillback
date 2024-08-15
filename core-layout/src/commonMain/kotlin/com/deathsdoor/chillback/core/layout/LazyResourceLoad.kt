package com.deathsdoor.chillback.core.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable


@Composable
@NonRestartableComposable
expect fun LazyResource(content : @Composable LazyResourceLoader.() -> Unit)