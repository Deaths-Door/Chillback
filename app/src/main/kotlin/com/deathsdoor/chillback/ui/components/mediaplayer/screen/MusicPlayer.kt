package com.deathsdoor.chillback.ui.components.mediaplayer.screen

import StackedSnackbarDuration
import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerExpandableCard(modifier: Modifier = Modifier) = MusicPlayerShared { coroutineScope, mediaController, currentMediaItem ->
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    CollapsedMediaPlayer(
        modifier = modifier.padding(8.dp),
        mediaController = mediaController,
        currentMediaItem = currentMediaItem,
        onClick = {
            isSheetOpen = true
            coroutineScope.launch {
                sheetState.show()
            }
        }
    )

    if(!isSheetOpen) return@MusicPlayerShared

    val onDismiss = {
        isSheetOpen = false
        coroutineScope.launch {
            sheetState.hide()
        };
        Unit
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = null,
        content = {
            ExpandedMediaPlayer(
                mediaController = mediaController,
                currentMediaItem = currentMediaItem,
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
fun MusicPlayerBar(modifier: Modifier = Modifier) = MusicPlayerShared { coroutineScope, mediaController, currentMediaItem ->
    CollapsedDesktopMediaPlayer(
        modifier = modifier,
        mediaController = mediaController,
        currentMediaItem = currentMediaItem,
        coroutineScope = coroutineScope
    )
}

@Composable
private fun MusicPlayerShared(content : @Composable (coroutineScope : CoroutineScope,mediaController : MediaController,currentMediaItem : MediaItem) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val mediaController = LocalAppState.current.mediaController
    val currentMediaItem by mediaController.currentMediaItemAsFlow(coroutineScope).collectAsState()

    AnimatedVisibility(visible = currentMediaItem != null) {
        mediaController!!.DisplayAudioLevelWarning()

        content(coroutineScope,mediaController,currentMediaItem!!)
    }
}

fun MediaController?.currentMediaItemAsFlow(scope: CoroutineScope) : StateFlow<MediaItem?> = flow {
    while (true) {
        emit(this@currentMediaItemAsFlow?.currentMediaItem)
        delay(1000)
    }
}.distinctUntilChanged().stateIn(
    scope = scope,
    initialValue = null,
    started = SharingStarted.WhileSubscribed(5000L)
)

@Composable
private fun MediaController.DisplayAudioLevelWarning() {
    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val snackbarState = LocalSnackbarState.current

    DisposableEffect(Unit) {
        // Show warning to user if volume is to low
        val showSnackbar = {
            snackbarState.showWarningSnackbar(
                title = "Attention Required: Low Device Volume",
                description = "We noticed your device volume is low. For an optimal audio experience, please adjust the volume using the volume buttons on your device.",
                duration = StackedSnackbarDuration.Long
            )
        }

        val playerListener = object : Player.Listener {
            private inline val volumeConditionTrue get() = audioManager.getStreamVolume(STREAM_MUSIC) < 25
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if(isPlaying && volumeConditionTrue) showSnackbar()
            }

            override fun onVolumeChanged(volume: Float) {
                super.onVolumeChanged(volume)
                if(volumeConditionTrue) showSnackbar()
            }
        }
        this@DisplayAudioLevelWarning.addListener(playerListener)
        onDispose { this@DisplayAudioLevelWarning.removeListener(playerListener) }
    }
}