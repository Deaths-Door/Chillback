package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.media3.session.MediaController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.media3.common.MediaMetadata
import com.deathsdoor.chillback.ui.components.MarqueeText

@Composable
fun MediaTitle(
    modifier: Modifier = Modifier,
    iconModifier : Modifier = Modifier,
    mediaController: MediaController,
    currentMediaItem: MediaMetadata,
    titleStyle : TextStyle,
    artistStyle : TextStyle
) {
    Column(
        modifier = modifier,
        content = {
            MarqueeText(
                modifier = Modifier.fillMaxWidth(),
                text = currentMediaItem.title.toString(),
                style = titleStyle,
                fontWeight = FontWeight.Bold
            )

            MarqueeText(
                modifier = Modifier.fillMaxWidth(),
                text = currentMediaItem.artist.toString(),
                style = artistStyle
            )
        }
    )

    LikeButton(
        modifier = iconModifier,
        mediaController = mediaController,
    )
}