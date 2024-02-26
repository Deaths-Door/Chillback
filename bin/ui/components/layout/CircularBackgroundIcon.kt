package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.jetbrains.annotations.Async

@Composable
@NonRestartableComposable
fun CircularBackgroundIconButton(
    painter: Painter,
    contentDescription : String? = null,
    onClick : () -> Unit
) = IconButton(
    modifier = Modifier.background(themeBasedTint())
        .padding(16.dp)
        .clip(CircleShape),
    content = { Icon(painter = painter,contentDescription = contentDescription) },
    onClick = onClick
)

@Composable
@NonRestartableComposable
fun CircularBackgroundIcon(
    painter: Painter,
    contentDescription : String? = null,
) = Icon(
    modifier = Modifier.background(themeBasedTint())
        .padding(16.dp)
        .clip(CircleShape),
    painter = painter,
    contentDescription = contentDescription
)


@Composable
@NonRestartableComposable
fun CircularBackgroundImage(
    modifier: Modifier = Modifier,
    model: Any,
    contentDescription : String? = null,
    background : Color = themeBasedTint()
) = AsyncImage(
    modifier = modifier
        .padding(16.dp)
        .clip(CircleShape)
        .background(background),
    model = model,
    contentDescription = contentDescription,
)