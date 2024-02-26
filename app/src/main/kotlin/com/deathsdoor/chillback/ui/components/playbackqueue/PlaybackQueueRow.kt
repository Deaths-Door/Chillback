package com.deathsdoor.chillback.ui.components.playbackqueue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.ui.components.track.TrackSmuggedArtworkCard
import com.deathsdoor.chillback.ui.components.track.TrackSongCount
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
fun PlaybackQueueRowSection(
    modifier : Modifier = Modifier,
    mediaController: MediaController
) {
    val state = rememberLazyListState()

    LaunchedEffect(mediaController.currentMediaItemIndex) {
        state.scrollToItem(mediaController.currentMediaItemIndex)
    }

    val playbackQueueFlow by LocalAppState.current.musicRepository.playbackQueue.observe(
        coroutineScope = rememberCoroutineScope(),
        mediaController = mediaController
    ).collectAsState()

    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    Text(
                        text = "Playback Queue",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )

                    TrackSongCount(
                        modifier = Modifier.weight(1f),
                        count = playbackQueueFlow,
                        textAlign = TextAlign.End
                    )

                    IconButton(
                        onClick = { TODO("NAVIGATE TO PLAYBACK QUEUE") },
                        content = {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Navigate to Playback Queue",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    )
                }
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                contentPadding = PaddingValues(horizontal = 10.dp),
                content = {
                    itemsIndexed(items = playbackQueueFlow) { index, track ->
                        TrackSmuggedArtworkCard(
                            modifier = Modifier.width(224.dp).padding(vertical = 16.dp),
                            details = track,
                            onClick = { mediaController.seekTo(index, 0) }
                        )
                    }
                }
            )
        }
    )
}