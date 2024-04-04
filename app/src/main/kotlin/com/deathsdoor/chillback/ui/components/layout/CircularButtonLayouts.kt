package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.extensions.themeBasedTint

@Composable
@NonRestartableComposable
fun CircularBackgroundIconButton(
    modifier : Modifier = Modifier,
    painter: Painter,
    contentDescription : String? = null,
    tint : Color = themeBasedTint(),
    onClick : () -> Unit
) = CircularBackgroundButton(
    modifier = modifier,
    onClick = onClick,
    content = { Icon(painter = painter,contentDescription = contentDescription,tint = tint) }
)

@Composable
@NonRestartableComposable
fun CircularBackgroundImageVectorButton(
    modifier : Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription : String? = null,
    tint : Color = themeBasedTint(),
    onClick : () -> Unit
) = CircularBackgroundButton(
    modifier = modifier,
    onClick = onClick,
    content = { Icon(imageVector = imageVector,contentDescription = contentDescription,tint = tint) }
)

@Composable
@NonRestartableComposable
fun CircularBackgroundIconButtonWithText(
    modifier : Modifier = Modifier,
    painter: Painter,
    contentDescription : String? = null,
    tint : Color = themeBasedTint(),
    text : @Composable () -> Unit,
    onClick : () -> Unit
) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    CircularBackgroundIconButton(
        modifier,
        painter,
        contentDescription,
        tint,
        onClick
    )
    text()
}

fun Modifier.circular() = Modifier.padding(8.dp).clip(CircleShape) then this,

@Deprecated("Do not use", ReplaceWith(
    "IconButton(modifier = modifier.circular(), content = content, onClick = onClick)",
    "androidx.compose.material3.IconButton"
)
)
@Composable
@NonRestartableComposable
fun CircularBackgroundButton(
    modifier : Modifier,
    onClick : () -> Unit,
    content : @Composable () -> Unit
) = IconButton(
    modifier = modifier.circular(),
    content = content,
    onClick = onClick
)
