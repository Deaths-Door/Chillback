package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.deathsdoor.chillback.ui.screens.providers.LocalCoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsedMediaPlayer(){
    val mediaController = LocalCoreViewModel.current.mediaController

    /*// TODO : Remove this dummy code , used so that the player is shown at all
    {
        val dummyMediaUri =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val mediaItem = MediaItem.fromUri(dummyMediaUri)
        mediaController?.addMediaItem(mediaItem)
    }()*/

    // TODO : Use flows or remmember to make it actually update
    val currentMediaItem = (mediaController?.currentMediaItem ?: return).mediaMetadata

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetShown by remember { mutableStateOf(sheetState.isVisible) }

    Card(
        modifier = Modifier.padding(8.dp),
        onClick = {
            isSheetShown = true
            coroutineScope.launch { sheetState.show() }
        },
        content = {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    val iconSizeModifier = Modifier.size(52.dp)
                    TrackArtwork(
                        modifier = iconSizeModifier,
                        mediaController = mediaController
                    )

                    MediaTitle(
                        modifier = Modifier.weight(1f),
                        iconModifier = iconSizeModifier,
                        mediaController = mediaController,
                        currentMediaItem = currentMediaItem,
                        titleStyle = MaterialTheme.typography.bodyLarge,
                        artistStyle = LocalTextStyle.current
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

            // TODO : Add Slider back to UI
            // DurationSlider(mediaController = mediaController)
        }
    )

    if (!isSheetShown) return

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            isSheetShown = false
            coroutineScope.launch { sheetState.hide() }
        },
        content = { ExtendedMediaPlayer(mediaController,currentMediaItem) }
    )
}