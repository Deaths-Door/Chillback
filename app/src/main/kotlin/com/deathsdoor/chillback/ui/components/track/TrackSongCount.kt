package com.deathsdoor.chillback.ui.components.track

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
@NonRestartableComposable
fun <T> TrackSongCount(
    modifier: Modifier = Modifier,
    count : Collection<T>?,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodySmall
) = Text(
    modifier = modifier,
    text = "${count?.size ?: 0} songs",
    textAlign = textAlign,
    style = style,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
)