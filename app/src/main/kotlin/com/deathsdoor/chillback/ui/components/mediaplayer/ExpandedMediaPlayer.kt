package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController

// TODO : Depeneding on how it goes decide whether to keep LazyColumn as parent element
@Composable
fun ExtendedMediaPlayer(
    mediaController: MediaController,
    currentMediaItem: MediaMetadata,
) = LazyColumn(
    modifier = Modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    content = {
        item { Header(mediaController) }
        item { CoreMediaPlayer(mediaController,currentMediaItem) }
        // TODO : Add Lyrics View + Choose which device to playon + Share Song + View Queue Options + Equalizers
    }
)

@Composable
private fun Header(mediaController: MediaController) = Row(
    modifier = Modifier.padding(8.dp),
    content = {
        Column(
            modifier = Modifier.weight(1f),
            content = {
                Text(
                    text = "PLAYING FROM",
                    style = MaterialTheme.typography.labelLarge
                )

                Text(text = mediaController.currentMediaItem?.mediaMetadata?.albumTitle.toString())
            }
        )

        // TODO : Make this so it shows some options
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Click to get extra options"
        )
    }
)

@Composable
private fun CoreMediaPlayer(
    mediaController: MediaController,
    currentMediaItem: MediaMetadata,
) {
    TrackArtwork(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        mediaController = mediaController
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        MediaTitle(
            modifier = Modifier.weight(1f),
            mediaController = mediaController,
            currentMediaItem = currentMediaItem,
            titleStyle = MaterialTheme.typography.headlineMedium,
            artistStyle = MaterialTheme.typography.titleMedium
        )
    }

    DurationSlider(
        modifier = Modifier.fillMaxWidth(),
        mediaController = mediaController
    )

    Row {
        Text(text = mediaController.currentPosition.format())

        Spacer(modifier = Modifier.weight(1f))

        Text(text = mediaController.duration.format())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val iconSizeModifier = Modifier.size(72.dp)

        ShuffleButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        PreviousMediaItemButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        PlayPauseButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        NextMediaItemButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        RepeatMediaItemsButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )
    }
}

fun Long.format() : String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d",minutes, seconds)
}