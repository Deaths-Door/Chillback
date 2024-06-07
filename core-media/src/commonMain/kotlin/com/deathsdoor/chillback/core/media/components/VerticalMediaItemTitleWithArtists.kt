package com.deathsdoor.chillback.core.media.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.deathsdoor.astroplayer.core.AstroMediaItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalMediaItemTitleWithArtists(
    modifier: Modifier = Modifier,
    mediaItem: AstroMediaItem,
    titleStyle : TextStyle,
    artistStyle : TextStyle
) = Column(
    modifier = modifier,
    content = {
        val contentModifier = Modifier.basicMarquee().fillMaxWidth()

        Text(
            modifier = contentModifier,
            text = mediaItem.metadata?.title.toString(),
            style = titleStyle,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = contentModifier,
            text =  mediaItem.metadata?.artist.toString(),
            style = artistStyle
        )
    }
)