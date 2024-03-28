package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.media3.common.MediaItem

@Composable
fun MediaTitleWithArtist(
    modifier: Modifier = Modifier,
    currentMediaItem: MediaItem,
    titleStyle : TextStyle,
    artistStyle : TextStyle
) = Column(
    modifier = modifier,
    content = {
        Text(
            modifier = Modifier.basicMarquee().fillMaxWidth(),
            text = currentMediaItem.mediaMetadata.title.toString(),
            style = titleStyle,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.basicMarquee().fillMaxWidth(),
            text = currentMediaItem.mediaMetadata.artist.toString(),
            style = artistStyle
        )
    }
)