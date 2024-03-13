package com.deathsdoor.chillback.ui.components.mediaplayer.screen

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptions
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWarningSnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// TODO : CHECK IF MUSIC PREFERENcE SAVING IS WORKING
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(modifier : Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val mediaController = LocalAppState.current.mediaController
    val currentMediaItem by mediaController.currentMediaItemAsFlow(coroutineScope).collectAsState()

    if(currentMediaItem == null) return

    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    // Show warning to user if volume is to low
    val warningSnackbar = LocalWarningSnackbarState.current
    DisposableEffect(Unit) {
        Log.d("ui","warnign snackbar for volume low")
        val message = { "Your device volume is low. Please adjust for optimal audio experience." }
        val playerListener = object : Player.Listener {
            private inline val volumeConditionTrue get() = audioManager.getStreamVolume(STREAM_MUSIC) < 25
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if(isPlaying && volumeConditionTrue) coroutineScope.launch { warningSnackbar.showSnackbar(message()) }
            }

            override fun onVolumeChanged(volume: Float) {
                super.onVolumeChanged(volume)
                if(volumeConditionTrue) coroutineScope.launch { warningSnackbar.showSnackbar(message()) }
            }
        }
        mediaController!!.addListener(playerListener)
        onDispose { mediaController.removeListener(playerListener) }
    }

    val sheetState = rememberModalOptionsState(coroutineScope = coroutineScope,skipPartiallyExpanded = true)

    CollapsedMediaPlayer(
        modifier = modifier,
        mediaController = mediaController!!,
        currentMediaItem = currentMediaItem!!,
        onClick = { sheetState.show() }
    )

    ModalOptions(
        state = sheetState,
        dragHandle = null,
        content = {
            ExpandedMediaPlayer(
                sheetState = sheetState,
                mediaController = mediaController,
                currentMediaItem = currentMediaItem!!
            )
        }
    )
}

fun MediaController?.currentMediaItemAsFlow(scope: CoroutineScope) : StateFlow<MediaItem?> = flow {
    while (true) {
        emit(this@currentMediaItemAsFlow?.currentMediaItem)
        delay(1000)
    }
}.distinctUntilChanged()
    .stateIn(
    scope = scope,
    initialValue = null,
    started = SharingStarted.WhileSubscribed(5000L)
)