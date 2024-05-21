package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deathsdoor.astroplayer.ui.PlayButton
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.media.extensions.optionsItemSpacing
import com.deathsdoor.chillback.core.media.icons.PlayNext
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
internal fun PlayNowThumbItem(onClick : () -> Unit) {
    val label = stringResource(Res.strings.play_now)

    Thumbnail(
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = label)
            .optionsItemSpacing(),
        artwork = {
            PlayButton(enabled = false,onClick = {})
        },
        title = { ThumbnailTitle(text = label) }
    )
}

@Composable
internal fun PlayNowDropDownItem(onClick : () -> Unit) = DropdownMenuItem(
    text = { Text(stringResource(Res.strings.play_now)) },
    onClick = onClick,
    leadingIcon = {
        PlayButton(enabled = false,onClick = {})
    }
)

@Composable
internal fun PlayNextThumbItem(onClick : () -> Unit) {
    val label = stringResource(Res.strings.play_next)
    Thumbnail(
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = label)
            .optionsItemSpacing(),
        artwork = {
            Icon(
                imageVector = Icons.PlayNext,
                contentDescription = null
            )
        },
        title = { ThumbnailTitle(text = label) }
    )
}

@Composable
internal fun PlayNextDropDownItem(onClick : () -> Unit) = DropdownMenuItem(
    text = { Text(stringResource(Res.strings.play_now)) },
    onClick = onClick,
    leadingIcon = {
        Icon(
            imageVector = Icons.PlayNext,
            contentDescription = null
        )
    }
)