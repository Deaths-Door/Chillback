package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.deathsdoor.astroplayer.core.RepeatMode
import com.deathsdoor.astroplayer.core.RepeatMode.*
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.feature.mediaplayer.icons.RepeatOffOrOn
import com.deathsdoor.chillback.feature.mediaplayer.icons.RepeatOne
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

@Composable
internal fun RepeatMediaItemsButton(
    state : AstroPlayerState,
    modifier: Modifier = Modifier,
) = IconButton(
    modifier = modifier,
    colors = IconButtonDefaults.iconButtonColors(
        contentColor = if(state.repeatMode == Off) LocalContentColor.current else MaterialTheme.colorScheme.primary,
    ),
    onClick = { state.astroPlayer.repeatMode = state.repeatMode.cyclicNext() },
    content = {
        val (imageVector,contentDescription) = when(state.repeatMode) {
            Off -> Icons.RepeatOffOrOn to Res.strings.repeat_mode_off
            All -> Icons.RepeatOffOrOn to Res.strings.repeat_mode_all
            One -> Icons.RepeatOne to Res.strings.repeat_mode_one
        }

        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription)
        )
    }
)

private fun RepeatMode.cyclicNext(): RepeatMode {
    val entries = RepeatMode.entries
    return entries[(ordinal + 1) % entries.size]
}