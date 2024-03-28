package com.deathsdoor.chillback.ui.components.track

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Deprecated("Do not use")
@Composable
@NonRestartableComposable
fun <T> TrackSongCount(
    modifier: Modifier = Modifier,
    count : Collection<T>?,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodySmall
) = TrackSongCount(
    modifier = modifier,
    count = count?.size ?: 0,
    textAlign = textAlign,
    style = style
)

@Composable
@NonRestartableComposable
fun TrackSongCount(
    modifier: Modifier = Modifier,
    count : Int,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodySmall
) = Text(
    modifier = modifier,
    text = "$count songs",
    textAlign = textAlign,
    style = style,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
)