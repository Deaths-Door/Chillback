package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun HorizontalDurationMarkers(
    state : AstroPlayerState,
    modifier : Modifier = Modifier,
    content : (@Composable RowScope.() -> Unit)? = null
) = Row(modifier = modifier,horizontalArrangement = Arrangement.SpaceBetween) {
    val currentPosition by rememberCurrentPosition(state)
    val contentDuration by rememberContentDuration(state)

    Text(text = currentPosition)
    content?.invoke(this)
    Text(text = contentDuration)
}

@Composable
private fun rememberCurrentPosition(state : AstroPlayerState) = remember(state.astroPlayer.currentPosition) {
    mutableStateOf(state.astroPlayer.currentPosition.formatAsTime())
}
@Composable
private fun rememberContentDuration(state : AstroPlayerState) = remember(state.astroPlayer.contentDuration) {
    mutableStateOf(state.astroPlayer.contentDuration.formatAsTime())
}

private fun Long.formatAsTime() = milliseconds.toComponents { hours, minutes, seconds, _ ->
    String.format(
        "%02d:%02d:%02d",
        hours,
        minutes,
        seconds,
    )
}
