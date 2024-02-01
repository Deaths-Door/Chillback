package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.media3.common.MediaItem
import com.deathsdoor.chillback.ui.components.layout.MarqueeText

@Composable
fun MediaTitleWithArtist(
    modifier: Modifier = Modifier,
    currentMediaItem: MediaItem,
    titleStyle : TextStyle,
    artistStyle : TextStyle
) = Column(
    modifier = modifier,
    content = {
        MarqueeText(
            modifier = Modifier.fillMaxWidth(),
            text = currentMediaItem.mediaMetadata.title.toString(),
            style = titleStyle,
            fontWeight = FontWeight.Bold
        )

        MarqueeText(
            modifier = Modifier.fillMaxWidth(),
            text = currentMediaItem.mediaMetadata.artist.toString(),
            style = artistStyle
        )
    }
)