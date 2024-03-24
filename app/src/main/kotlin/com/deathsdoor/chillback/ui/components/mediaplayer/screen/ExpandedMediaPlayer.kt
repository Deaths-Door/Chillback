package com.deathsdoor.chillback.ui.components.mediaplayer.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.deathsdoor.chillback.data.extensions.PreviewMediaController
import com.deathsdoor.chillback.ui.components.action.DownButton
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
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
import kotlinx.coroutines.launch

// TODO : make this adaptive
// TODO : COMPLETE EACH button and screen here so that is really works
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpandedMediaPlayer(
    onDismiss : () -> Unit,
    mediaController : Player,
    currentMediaItem : MediaItem
) = Column {
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        navigationIcon = { DownButton(onClick = onDismiss) },
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Playing","Lyrics").forEachIndexed { index, s ->
                    TextButton(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        content = { Text(s) }
                    )
                }
            }
        },
        actions = { MoreInfoButton { /*TODO : Show more info about song and this view */ } }
    )

    DropdownMenuItem(text = { /*TODO*/ }, onClick = { /*TODO*/ })

    HorizontalPager(
        state = pagerState,
        pageContent = {
            when(it) {
                0 -> Playing(
                    coroutineScope = coroutineScope,
                    mediaController = mediaController,
                    currentMediaItem = currentMediaItem
                )
                1 -> TODO("SHOW LYRICS")
                else -> throw UnsupportedOperationException("Page $it not supported")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Playing(
    coroutineScope : CoroutineScope,
    mediaController : Player,
    currentMediaItem : MediaItem
) = Column(modifier = Modifier.padding(16.dp)) {
    TrackArtwork(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        currentMediaItem = currentMediaItem
    )

    val iconSizeModifier = Modifier.size(72.dp)

    Row {
        MediaTitleWithArtist(
            modifier = Modifier.weight(1f),
            currentMediaItem = currentMediaItem,
            titleStyle = MaterialTheme.typography.displaySmall,
            artistStyle = MaterialTheme.typography.titleMedium
        )

        LikeButton(
            modifier = iconSizeModifier,
            currentMediaItem = currentMediaItem,
            mediaController = mediaController
        )

        EqualizerButton(modifier = iconSizeModifier)
    }

    DurationSlider(mediaController)

    DurationMarkers(
        mediaController = mediaController,
        coroutineScope = coroutineScope
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
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

    Row(modifier = Modifier.fillMaxWidth()) {
        val modifier = Modifier.size(52.dp)

        ShowPlaybackQueueButton(
            modifier = modifier,
        )

        Spacer(modifier = Modifier.weight(1f))

        Share(
            modifier = modifier,
            //mediaItem = currentMediaItem
        )
    }

    Spacer(modifier = Modifier.fillMaxHeight())
}

@PreviewScreenSizes
@Composable
private fun ExpandedMediaPlayerPreview() = InitializeProvidersForPreview {
    MaterialTheme {
        ExpandedMediaPlayer(
            onDismiss = { },
            mediaController = PreviewMediaController,
            currentMediaItem = MediaItem.EMPTY
        )
    }
}