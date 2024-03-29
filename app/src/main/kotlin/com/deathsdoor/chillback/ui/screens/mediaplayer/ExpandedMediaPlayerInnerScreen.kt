package com.deathsdoor.chillback.ui.screens.mediaplayer

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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.navigation.NavHostController
import com.deathsdoor.chillback.data.mediaplayer.collectFormatAsTimeAsState
import com.deathsdoor.chillback.data.mediaplayer.currentMediaItemPositionAsFlow
import com.deathsdoor.chillback.data.mediaplayer.formatAsTime
import com.deathsdoor.chillback.data.mediaplayer.rememberMediaItemDuration
import com.deathsdoor.chillback.data.navigation.ExpandedMediaPlayerRoutes
import com.deathsdoor.chillback.ui.components.layout.DownButton
import com.deathsdoor.chillback.ui.components.layout.MoreInfoButton
import com.deathsdoor.chillback.ui.components.layout.modalsheet.ModalSheetState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedMediaPlayerInnerScreen(
    navController: NavHostController,
    sheetState: ModalSheetState,
    mediaController : MediaController,
    currentMediaItem : MediaItem
) = Column {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }

    ExpandedMediaPlayerRoutes.DefaultHeader(
        sheetState = sheetState,
        pagerState = pagerState,
        coroutineScope = coroutineScope
    )

    HorizontalPager(
        state = pagerState,
        pageContent = {
            when(it) {
                0 -> Playing(
                    coroutineScope = coroutineScope,
                    navController = navController,
                    mediaController = mediaController,
                    currentMediaItem = currentMediaItem
                )
                1 -> TODO("SHOW LYRICS")
                else -> throw UnsupportedOperationException("Page $it not supported")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ExpandedMediaPlayerRoutes.Companion.DefaultHeader(sheetState: ModalSheetState, pagerState: PagerState, coroutineScope : CoroutineScope) = TopAppBar(
    navigationIcon = { DownButton { sheetState.dismissSheet() } },
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


@Composable
private fun Playing(
    coroutineScope : CoroutineScope,
    navController: NavHostController,
    mediaController : MediaController,
    currentMediaItem : MediaItem
) = Column(modifier = Modifier.padding(16.dp)) {
    TrackArtwork(
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
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

        LikeButton(modifier = iconSizeModifier)

        EqualizerButton(
            modifier = iconSizeModifier,
            coroutineScope = coroutineScope
        )
    }

    DurationSlider(mediaController)

    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
        val currentPosition by mediaController.currentMediaItemPositionAsFlow(coroutineScope).collectFormatAsTimeAsState()

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
        with(Modifier.size(52.dp)) {
            ShowPlaybackQueueButton(
                modifier = this,
                onClick = { navController.navigate(ExpandedMediaPlayerRoutes.PlaybackQueue.route) }
            )

            Spacer(modifier = Modifier.weight(1f))

            ShareMediaItem(
                modifier = this,
                mediaItem = currentMediaItem
            )
        }
    }

    Spacer(modifier = Modifier.fillMaxHeight())
}