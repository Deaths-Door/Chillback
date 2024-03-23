package com.deathsdoor.chillback.ui.components.action

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.Thumbnail

@Composable
fun PlayNowThumbItem(onClick : () -> Unit) {
    val label = "Play Now"
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
        title = label
    )
}

@Composable
fun PlayNextThumbItem(onClick : () -> Unit) {
    val label = "Play Next"
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
        title = label
    )
}