package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import com.deathsdoor.chillback.data.mediaplayer.currentMediaItemAsFlow
import com.deathsdoor.chillback.data.viewmodel.CoreViewModel
import com.deathsdoor.chillback.ui.components.layout.modalsheet.ModalSheet
import com.deathsdoor.chillback.ui.components.layout.modalsheet.rememberModalSheetState
import com.deathsdoor.chillback.ui.providers.LocalCoreViewModel
import com.deathsdoor.chillback.ui.screens.mediaplayer.ExpandedMediaPlayer


@Deprecated("USE THIS ONLY FOR TESTINGS")
fun dummyCode(coreViewModel : CoreViewModel) {
    val dummyMediaUri =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    val mediaItem = MediaItem.fromUri(dummyMediaUri)
    coreViewModel.mediaController?.addMediaItem(mediaItem)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPlayer(modifier: Modifier = Modifier) {
    val coreViewModel = LocalCoreViewModel.current
    val coroutineScope = rememberCoroutineScope()

    val currentMediaItem by coreViewModel.mediaController.currentMediaItemAsFlow(coroutineScope).collectAsState(null)

    // TODO : Remove dummy code when i don't need to 'view' the music player for UI testing
    dummyCode(coreViewModel)

    if(currentMediaItem == null) return

    val mediaController = coreViewModel.mediaController!!

    val sheetState = rememberModalSheetState(
        coroutineScope = coroutineScope,
        skipPartiallyExpanded = true
    )

    CollapsedMediaPlayer(
        modifier = modifier,
        mediaController = mediaController,
        currentMediaItem = currentMediaItem!!,
        onClick = { sheetState.showSheet() }
    )

    ModalSheet(sheetState, dragHandle = null) {
        ExpandedMediaPlayer(
            sheetState = sheetState,
            mediaController = mediaController,
            currentMediaItem = currentMediaItem!!
        )
    }
}