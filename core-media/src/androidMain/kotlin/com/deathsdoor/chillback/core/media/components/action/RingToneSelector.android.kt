package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.media.components.DraggableBar
import com.deathsdoor.chillback.core.media.components.LoadAmplitudes
import com.deathsdoor.chillback.core.media.components.RequestRingtonePermissions
import com.deathsdoor.chillback.core.media.extensions.isOnDevice
import com.deathsdoor.chillback.core.media.extensions.optionsItemSpacing
import com.deathsdoor.chillback.core.media.extensions.updateRingtone
import com.deathsdoor.chillback.core.media.icons.Notifications
import com.deathsdoor.chillback.core.media.resources.Res
import com.linc.audiowaveform.AudioWaveform
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
internal actual fun RingtoneSelectorDropDownItem(
    applicationPlayer: AstroPlayer,
    mediaItem: AstroMediaItem
){
    val label = stringResource(Res.strings.set_as_ringtone)
    val isDialogOpen = remember { mutableStateOf(false) }

    DropdownMenuItem(
        text = { Text(label) },
        enabled = !mediaItem.isOnDevice(),
        onClick = { isDialogOpen.value = true },
        leadingIcon = {
            Icon(
                imageVector = Icons.Notifications,
                contentDescription = label
            )
        },
    )

    if(!isDialogOpen.value) return

    RingtoneSelectorAlertDialog(applicationPlayer,isDialogOpen,mediaItem,label)
}

@Composable
internal actual fun RingtoneSelectorThumbItem(
    applicationPlayer: AstroPlayer,
    mediaItem: AstroMediaItem
) {
    val label = stringResource(Res.strings.set_as_ringtone)
    val isDialogOpen = remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable(onClickLabel = label, enabled = !mediaItem.isOnDevice()) {
                isDialogOpen.value = true
            }
            .optionsItemSpacing(),
        artwork = {
            Icon(
                imageVector = Icons.Notifications,
                contentDescription = label
            )
        },
        title = { ThumbnailTitle(text = label) }
    )

    if(!isDialogOpen.value) return

    RingtoneSelectorAlertDialog(applicationPlayer,isDialogOpen, mediaItem,label)
}

@Composable
internal actual fun RingtoneSelectorAlertDialog(
    applicationPlayer: AstroPlayer,
    isDialogOpen: MutableState<Boolean>,
    mediaItem: AstroMediaItem,
    label : String
) = RequestRingtonePermissions {
    LoadAmplitudes(
        applicationPlayer = applicationPlayer,
        mediaItem = mediaItem,
        content = { state, resource, amplitudes, totalDuration ->
            // TODO; repeat media with this
            val startProgress = remember { mutableFloatStateOf(0f) }
            val finishProgress = remember { mutableFloatStateOf(1f) }

            AlertDialog(
                onDismissRequest = { isDialogOpen.value = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
                title = { Text(label) },
                text = {
                    val maxWidth = Modifier.fillMaxWidth()
                    var waveformProgress by remember { mutableFloatStateOf(0f) }

                    Column {
                        Box(modifier = maxWidth) {
                            AudioWaveform(
                                progressBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                waveformBrush = SolidColor(Color.LightGray),
                                spikeWidth = 4.dp,
                                spikePadding = 2.dp,
                                spikeRadius = 4.dp,
                                progress = waveformProgress,
                                amplitudes = amplitudes,
                                onProgressChange = { waveformProgress = it },
                                onProgressChangeFinished = { state.astroPlayer.seekTo((totalDuration * waveformProgress).toLong()) }
                            )

                            // To ensure that both the draggable bars remain on their respective sides
                            LaunchedEffect(startProgress.floatValue) {
                                startProgress.floatValue =
                                    startProgress.floatValue.coerceAtMost(finishProgress.floatValue)
                            }

                            LaunchedEffect(finishProgress.floatValue) {
                                finishProgress.floatValue =
                                    finishProgress.floatValue.coerceAtLeast(startProgress.floatValue)
                            }

                            DraggableBar(progress = startProgress)
                            DraggableBar(progress = finishProgress)
                        }
                    }
                },
                confirmButton = {
                    val context = LocalContext.current
                    val snackBarState = LocalSnackbarState.current

                    val coroutineScope = rememberCoroutineScope()

                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                context.updateRingtone(
                                    resource = resource,
                                    contentDuration = state.astroPlayer.contentDuration,
                                    mediaItem = state.currentMediaItem!!,
                                    stackableSnackbarState = snackBarState,
                                    startProgress = startProgress,
                                    finishProgress = finishProgress
                                )
                            }
                        },
                        content = {
                            Text(
                                text = label,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            )
        }
    )
}