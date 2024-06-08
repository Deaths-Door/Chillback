package com.deathsdoor.chillback.feature.mediaplayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.deathsdoor.astroplayer.core.addListener
import com.deathsdoor.astroplayer.core.listeners.AstroListener
import com.deathsdoor.astroplayer.core.removeListener
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarDuration
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

// TODO : SYNCED PLAYBACK using KAFKA https://docs.confluent.io/kafka-clients/java/current/overview.html#java-installation
@Composable
internal fun MusicPlayerShared(state: AstroPlayerState,content : @Composable () -> Unit) {
    AnimatedVisibility(state.currentMediaItem != null) {
        content()

        val snackBarState = LocalSnackbarState.current

        LazyResource {
            DisposableEffect(Unit) {
                val id = state.astroPlayer.addListener(object : AstroListener {
                    fun informUserOnLowVolume() {
                        if(state.astroPlayer.volume < 0.25f) {
                            snackBarState.showWarningSnackbar(
                                title = stringResource(Res.strings.alert_low_volume),
                                description = stringResource(Res.strings.alert_low_volume_description),
                                duration = StackableSnackbarDuration.Long
                            )
                        }
                    }

                    override fun onVolumeChanged(volume: Float) {
                        super.onVolumeChanged(volume)
                        informUserOnLowVolume()
                    }

                    override fun onPlaybackStarted() {
                        super.onPlaybackStarted()
                        informUserOnLowVolume()
                    }
                })

                onDispose { state.astroPlayer.removeListener(id) }
            }
        }
    }
}