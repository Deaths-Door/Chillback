package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle

@Composable
fun PlayNowThumbItem(onClick : () -> Unit) {
    val label = stringResource(R.string.play_now)
    Thumbnail(
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = label)
            .optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.media3_notification_play),
                contentDescription = null
            )
        },
        title = { ThumbnailTitle(text = label) }
    )
}

@Composable
fun PlayNowDropDownItem(onClick : () -> Unit) = DropdownMenuItem(
    text = { Text(stringResource(R.string.play_now)) },
    onClick = onClick,
    leadingIcon = {
        Icon(
            painter = painterResource(id = R.drawable.media3_notification_play),
            contentDescription = null
        )
    }
)

@Composable
fun PlayNextThumbItem(onClick : () -> Unit) {
    val label = stringResource(R.string.play_next)
    Thumbnail(
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = label)
            .optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.playnext_icon_176700),
                contentDescription = null
            )
        },
        title = { ThumbnailTitle(text = label) }
    )
}

@Composable
fun PlayNexDropDownItem(onClick : () -> Unit) = DropdownMenuItem(
    text = { Text(stringResource(R.string.play_now)) },
    onClick = onClick,
    leadingIcon = {
        Icon(
            painter = painterResource(id = R.drawable.media3_notification_play),
            contentDescription = null
        )
    }
)