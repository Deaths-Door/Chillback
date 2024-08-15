package com.deathsdoor.chillback.core.media.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.media3.exoplayer.ExoPlayer
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.NativeMediaPLayer
import com.deathsdoor.astroplayer.core.addListener
import com.deathsdoor.astroplayer.core.listeners.AstroListener
import com.deathsdoor.astroplayer.core.removeListener
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.layout.AnimatedUndismissibleLoadingContent
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarDuration
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.core.media.resources.Res
import com.spr.jetpack_loading.components.indicators.BallPulseRiseIndicator
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaResult
import linc.com.amplituda.Cache
import linc.com.amplituda.callback.AmplitudaErrorListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoadAmplitudes(applicationPlayer: AstroPlayer, mediaItem : AstroMediaItem, content : @Composable (state : AstroPlayerState, resource : LazyResourceLoader, amplitudes : SnapshotStateList<Int>, totalDuration : Long) -> Unit) {
    val context = LocalContext.current
    val snackBarState = LocalSnackbarState.current
    val state = remember {
        val exoplayer = ExoPlayer.Builder(context).build().apply { prepare() }
        val astroPlayer = AstroPlayer(NativeMediaPLayer(exoplayer))
        astroPlayer.addMediaItem(mediaItem)
        AstroPlayerState(astroPlayer)
    }

    DisposableEffect(Unit) {
        // Basically pause the app player when playing a child player so that the sounds do not overlap
        val id = state.astroPlayer.addListener(object : AstroListener {
            override fun onPlaybackStarted() {
                super.onPlaybackStarted()
                applicationPlayer.pause()
            }
        })

        onDispose {
            state.astroPlayer.removeListener(id)
            state.astroPlayer.release()
        }
    }

    LazyResource {
        val amplitudes = remember { mutableStateListOf<Int>() }
        var totalDuration by remember { mutableLongStateOf(-1L) }

        LaunchedEffect(Unit) {
            val amplituda = Amplituda(context)
            amplituda.audioData(
                mediaItem = mediaItem,
                snackBarState = snackBarState,
                resource = this@LazyResource,
                onSuccess = {
                    totalDuration = it.getAudioDuration(AmplitudaResult.DurationUnit.MILLIS)
                    amplitudes.addAll(it.amplitudesAsList())
                    state.astroPlayer.addMediaItem(mediaItem)
                }
            )
        }

        when {
            amplitudes.isEmpty() -> AnimatedUndismissibleLoadingContent(label = stringResource(Res.strings.fetching_media_info))
            else -> content(state,this@LazyResource, amplitudes,totalDuration)
        }
    }
}

private fun Amplituda.audioData(
    mediaItem: AstroMediaItem,
    snackBarState : StackableSnackbarState,
    resource : LazyResourceLoader,
    onSuccess : (AmplitudaResult<String>) -> Unit
): Unit = onSuccess(
    processAudio(
        mediaItem.source.toString(),
        Cache.withParams(Cache.REUSE),
    ).get(AmplitudaErrorListener {
        snackBarState.showErrorSnackbar(
            title = resource.stringResource(Res.strings.unable_to_open_file),
            description = resource.stringResource(Res.strings.try_again),
            duration = StackableSnackbarDuration.Long,
            actionTitle = resource.stringResource(Res.strings.retry),
            action = {
                this@audioData.audioData(
                    mediaItem = mediaItem,
                    snackBarState = snackBarState,
                    resource = resource,
                    onSuccess = onSuccess
                )
            },
        )
    })
)