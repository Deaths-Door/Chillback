package com.deathsdoor.chillback.core.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

@Composable
@NonRestartableComposable
actual inline fun LazyResource(content: @Composable() (LazyResourceLoader.() -> Unit))= content { resource, args ->
    // Source Code of stringResource - iOS
    StringDesc.ResourceFormatted(resource, *args).localized()
}