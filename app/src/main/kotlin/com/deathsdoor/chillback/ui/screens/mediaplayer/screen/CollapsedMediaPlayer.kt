package com.deathsdoor.chillback.ui.components.mediaplayer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.extensions.PreviewMediaController
import com.deathsdoor.chillback.ui.components.action.Share
import com.deathsdoor.chillback.ui.components.mediaplayer.DurationMarkers
import com.deathsdoor.chillback.ui.components.mediaplayer.DurationSlider
import com.deathsdoor.chillback.ui.components.mediaplayer.EqualizerButton
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.components.mediaplayer.MediaTitleWithArtist
import com.deathsdoor.chillback.ui.components.mediaplayer.NextMediaItemButton
import com.deathsdoor.chillback.ui.components.mediaplayer.PlayPauseButton
import com.deathsdoor.chillback.ui.components.mediaplayer.PreviousMediaItemButton
import com.deathsdoor.chillback.ui.components.mediaplayer.RepeatMediaItemsButton
import com.deathsdoor.chillback.ui.components.mediaplayer.ShowPlaybackQueueButton
import com.deathsdoor.chillback.ui.components.mediaplayer.ShuffleButton
import com.deathsdoor.chillback.ui.components.mediaplayer.TrackArtwork
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import kotlinx.coroutines.CoroutineScope

private val ARROW_HEIGHT = 16.dp
private val ARROW_WIDTH = ARROW_HEIGHT * 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsedMediaPlayer(
    modifier : Modifier = Modifier,
    mediaController : Player,
    currentMediaItem : MediaItem,
    onClick : () -> Unit
) =  Column(modifier = modifier) {
    Card(
        modifier = Modifier
            .align(Alignment.End)
            .size(height = ARROW_HEIGHT, width = ARROW_WIDTH)
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    currentMediaItem = currentMediaItem,
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    artistStyle = LocalTextStyle.current
                )

                if(!LocalInspectionMode.current) LikeButton(
                    modifier = iconSizeModifier,
                    currentMediaItem = currentMediaItem,
                    mediaController = mediaController
                )

                RepeatMediaItemsButton(
                    modifier = iconSizeModifier,
                    mediaController = mediaController
                )

                Spacer(modifier = Modifier.width(8.dp))

                PlayPauseButton(
                    modifier = iconSizeModifier,
                    mediaController = mediaController
                )
            }
        )

        DurationSlider(
            modifier = Modifier.heightIn(max = 8.dp),
            mediaController = mediaController,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsedDesktopMediaPlayer(
    modifier : Modifier = Modifier,
    mediaController : Player,
    currentMediaItem : MediaItem,
    coroutineScope : CoroutineScope,
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    val iconSizeModifier = Modifier.size(52.dp)

    TrackArtwork(
        modifier = Modifier.size(128.dp),
        currentMediaItem = currentMediaItem
    )

    MediaTitleWithArtist(
        modifier = Modifier
            .padding(start = 8.dp),
        currentMediaItem = currentMediaItem,
        titleStyle = MaterialTheme.typography.headlineLarge,
        artistStyle = MaterialTheme.typography.bodyLarge
    )

    if(!LocalInspectionMode.current) LikeButton(
        modifier = iconSizeModifier,
        currentMediaItem = currentMediaItem,
        mediaController = mediaController
    )

    val _05Weight =Modifier.weight(0.5f)
    Spacer(modifier = _05Weight)

    Column(modifier = Modifier.weight(5f)) {
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
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

        DurationSlider(mediaController = mediaController)
        
        DurationMarkers(
            mediaController = mediaController,
            coroutineScope = coroutineScope
        )
    }

    Spacer(modifier = _05Weight)

    IconButton(
        modifier = iconSizeModifier,
        onClick = { /*TODO*/ },
        content = {
            Icon(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Look at the songs lyrics"
            )
        }
    )

    EqualizerButton()

    ShowPlaybackQueueButton(modifier = iconSizeModifier)

    Share()

    IconButton(
        modifier = iconSizeModifier,
        onClick = { /*TODO*/ },
        content = {
            Icon(
                painter = painterResource(id = R.drawable.zoom_pan),
                contentDescription = "Navigate to Full Screen MediaPlayer"
            )
        }
    )
}

@Preview
@PreviewLightDark
@Composable
private fun CollapsedMediaPlayerPreview() = InitializeProvidersForPreview {
    CollapsedMediaPlayer(
        mediaController = PreviewMediaController,
        currentMediaItem = MediaItem.EMPTY,
        onClick = {}
    )
}


@Preview(device = "spec:width=1000dp,height=500dp,dpi=440")
//@PreviewLightDark
@Composable
private fun CollapsedDesktopMediaPlayerPreview() = InitializeProvidersForPreview {
    CollapsedDesktopMediaPlayer(
        modifier = Modifier.fillMaxWidth(),
        mediaController = PreviewMediaController,
        currentMediaItem = MediaItem.EMPTY,
        coroutineScope = rememberCoroutineScope()
    )
}