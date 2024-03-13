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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.ui.components.action.DownButton
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
import com.deathsdoor.chillback.ui.components.mediaplayer.DurationSlider
import com.deathsdoor.chillback.ui.components.mediaplayer.EqualizerButton
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.components.mediaplayer.MediaTitleWithArtist
import com.deathsdoor.chillback.ui.components.mediaplayer.NextMediaItemButton
import com.deathsdoor.chillback.ui.components.mediaplayer.PlayPauseButton
import com.deathsdoor.chillback.ui.components.mediaplayer.PreviousMediaItemButton
import com.deathsdoor.chillback.ui.components.mediaplayer.RepeatMediaItemsButton
import com.deathsdoor.chillback.ui.components.mediaplayer.ShareMediaItem
import com.deathsdoor.chillback.ui.components.mediaplayer.ShowPlaybackQueueButton
import com.deathsdoor.chillback.ui.components.mediaplayer.ShuffleButton
import com.deathsdoor.chillback.ui.components.mediaplayer.TrackArtwork
import com.deathsdoor.chillback.ui.components.mediaplayer.rememberMediaItemDuration
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

// TODO : COMPLETE EACH button and screen here so that is really works
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpandedMediaPlayer(
    sheetState: ModalOptionsState,
    mediaController : MediaController,
    currentMediaItem : MediaItem
) = Column {
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        navigationIcon = { DownButton { sheetState.dismiss() } },
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
    mediaController : MediaController,
    currentMediaItem : MediaItem
) = Column(modifier = Modifier.padding(16.dp)) {
    TrackArtwork(
        modifier = Modifier.padding(16.dp)
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

    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
        val currentPosition by mediaController.currentMediaItemPositionAsFlow(coroutineScope).collectAsState()

        Text(text = currentPosition)

        val duration by rememberMediaItemDuration(mediaController)

        Text(text = duration.formatAsTime())
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        ShuffleButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        PreviousMediaItemButton(
            modifier = iconSizeModifier,
            mediaController = mediaController
        )

        PlayPauseButton(modifier = iconSizeModifier)

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

        ShareMediaItem(
            modifier = modifier,
            mediaItem = currentMediaItem
        )
    }

    Spacer(modifier = Modifier.fillMaxHeight())
}

private fun MediaController.currentMediaItemPositionAsFlow(scope: CoroutineScope): StateFlow<String> = flow {
    while (true) {
        emit(this@currentMediaItemPositionAsFlow.currentPosition.formatAsTime())
        delay(1000) // Update the media metadata every second
    }
}.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000L),
    initialValue = formatIntoTime(0,0,0),
)

private fun Long.formatAsTime() = milliseconds.toComponents { hours, minutes, seconds, _ -> formatIntoTime(hours,minutes,seconds) }
private fun formatIntoTime(hours : Long, minutes : Int, seconds : Int): String = String.format(
    "%02d:%02d:%02d",
    hours,
    minutes,
    seconds,
)