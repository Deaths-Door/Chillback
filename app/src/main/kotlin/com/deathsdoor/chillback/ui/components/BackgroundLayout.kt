package com.deathsdoor.chillback.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun BackgroundLayout(
    background : @Composable BoxScope.() -> Unit,
    content : @Composable BoxScope.() -> Unit
) = Box(
    modifier = Modifier.fillMaxSize(),
    content = {
        background()
        content()
    }
)

@Composable
@NonRestartableComposable
fun BackgroundImage(
    modifier: BoxScope.() -> Modifier = { Modifier },
    painter: Painter,
    content : @Composable BoxScope.() -> Unit
) = BackgroundLayout(
    background = {
        Image(
            modifier = modifier().matchParentSize(),
            painter = painter,
            contentDescription = null
        )
    },
    content = content
)