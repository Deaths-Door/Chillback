package com.deathsdoor.chillback.ui.components.mediaplayer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.ui.components.mediaplayer.DurationSlider
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.components.mediaplayer.MediaTitleWithArtist
import com.deathsdoor.chillback.ui.components.mediaplayer.PlayPauseButton
import com.deathsdoor.chillback.ui.components.mediaplayer.RepeatMediaItemsButton
import com.deathsdoor.chillback.ui.components.mediaplayer.TrackArtwork

private val ARROW_HEIGHT = 16.dp
private val ARROW_WIDTH = ARROW_HEIGHT * 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsedMediaPlayer(
    modifier : Modifier = Modifier,
    mediaController : MediaController,
    currentMediaItem : MediaItem,
    onClick : () -> Unit
) = Column(modifier = modifier.padding(8.dp)) {
    Card(
        modifier = Modifier.align(Alignment.End)
            .size(height = ARROW_HEIGHT,width = ARROW_WIDTH)
            .padding(end = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Expand Player"
            )
        }
    )

    Card {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                val iconSizeModifier = Modifier.size(52.dp)

                TrackArtwork(
                    modifier = iconSizeModifier,
                    currentMediaItem = currentMediaItem
                )

                MediaTitleWithArtist(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    currentMediaItem = currentMediaItem,
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    artistStyle = LocalTextStyle.current
                )

                LikeButton(
                    modifier = iconSizeModifier,
                    currentMediaItem = currentMediaItem,
                    mediaController = mediaController
                )

                RepeatMediaItemsButton(
                    modifier = iconSizeModifier,
                    mediaController = mediaController
                )

                Spacer(modifier = Modifier.width(8.dp))

                PlayPauseButton(modifier = iconSizeModifier)
            }
        )

        DurationSlider(
            modifier = Modifier.heightIn(max = 8.dp),
            mediaController = mediaController,
            colors = SliderDefaults.colors(inactiveTrackColor = Color.Red),
        )
    }
}