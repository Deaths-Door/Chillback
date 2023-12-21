package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
@NonRestartableComposable
fun BackgroundImage(
    modifier: Modifier = Modifier.fillMaxSize(),
    painter: Painter,
    content : @Composable BoxScope.() -> Unit
) = Box(
    modifier = Modifier.fillMaxSize(),
    content = {
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = null
        )

        content()
    }
)
