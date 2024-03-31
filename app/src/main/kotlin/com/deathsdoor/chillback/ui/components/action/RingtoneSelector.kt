package com.deathsdoor.chillback.ui.components.action

import StackedSnackbarDuration
import StackedSnakbarHostState
import android.content.ContentValues
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.components.mediaplayer.AnimatedAmplitudes
import com.deathsdoor.chillback.ui.components.mediaplayer.PlayPauseButton
import com.deathsdoor.chillback.ui.components.mediaplayer.rememberMediaControllerIsPlaying
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.linc.audiowaveform.AudioWaveform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaResult
import linc.com.amplituda.Cache
import linc.com.amplituda.callback.AmplitudaErrorListener

@Composable
fun RingtoneSelectorThumbItem(track: Track, details : TrackDetails){
    val label = stringResource(R.string.set_as_ringtone)
    val isDialogOpen = remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable(onClickLabel = label) {
                isDialogOpen.value = true
            }
            .optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.media3_notification_play),
                contentDescription = label
            )
        },
        title = { ThumbnailTitle(text = label) }
    )

    if(!isDialogOpen.value) return

    RingtoneSelectorAlertDialog(isDialogOpen = isDialogOpen, track = track, details = details)
}

@Composable
fun RingtoneSelectorDropDownItem(
    track: Track,
    enabled : Boolean,
    details : () -> TrackDetails
) {
    val label = stringResource(R.string.set_as_ringtone)
    val isDialogOpen = remember { mutableStateOf(false) }

    DropdownMenuItem(
        enabled = enabled,
        text = { Text(label) },
        onClick = { isDialogOpen.value = true },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.media3_notification_play),
                contentDescription = label
            )
        },
    )

    if(!isDialogOpen.value) return

    RingtoneSelectorAlertDialog(isDialogOpen = isDialogOpen, track = track, details = details())
}

private const val LINE_WIDTH = 8f

private fun Amplituda.audioData(
    track: Track,
    snackbarState : StackedSnakbarHostState,
    onSuccess : (AmplitudaResult<String>) -> Unit
): Unit = onSuccess(
    processAudio(
        track.sourcePath,
        Cache.withParams(Cache.REUSE),
    )
        .get(AmplitudaErrorListener {
        runBlocking {
            snackbarState.showErrorSnackbar(
                title = "Unable to open file",
                description = "Please try again. Cause: ${it.message}",
                duration = StackedSnackbarDuration.Long,
                actionTitle = "Retry",
                action = {
                    this@audioData.audioData(track,snackbarState,onSuccess)
                },
            )
        }
    })
)


@Composable
private fun RingtoneSelectorAlertDialog(isDialogOpen : MutableState<Boolean>,track: Track, details : TrackDetails) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { isDialogOpen.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text(stringResource(R.string.set_as_ringtone)) },
        text = {
            val snackbarState = LocalSnackbarState.current
            val appState = LocalAppState.current

            val mediaController = remember {
                ExoPlayer.Builder(context).build().apply { prepare() }
            }

            val amplitudes = remember { mutableStateListOf<Int>() }
            var totalDuration by remember { mutableLongStateOf(-1L) }

            DisposableEffect(Unit) {
                onDispose { mediaController.release() }
            }

            LaunchedEffect(Unit) {
                val amplituda = Amplituda(context)
                amplituda.audioData(
                    track = track,
                    snackbarState = snackbarState,
                    onSuccess = {
                        totalDuration = it.getAudioDuration(AmplitudaResult.DurationUnit.MILLIS)
                        amplitudes.addAll(it.amplitudesAsList())

                        mediaController.addMediaItem(details.asMediaItem(track))
                    }
                )
            }

            if(amplitudes.isEmpty()) {
                Text("Wait a second .. Fetching data!")
                return@AlertDialog
            }

            val modifier = Modifier.fillMaxWidth()

            var waveformProgress by remember { mutableStateOf<Float?>(null) }

            Column {
                val containerSize by remember { mutableStateOf(IntSize.Zero) }
                Box(modifier = modifier) {
                    val density = LocalDensity.current
                    var height by remember { mutableStateOf(Dp.Hairline) }

                    AudioWaveform(
                        modifier = Modifier.onSizeChanged {
                             height = with(density) { it.height.toDp() }
                        },
                        progressBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        waveformBrush = SolidColor(Color.LightGray),
                        spikeWidth = 4.dp,
                        spikePadding = 2.dp,
                        spikeRadius = 4.dp,
                        progress = waveformProgress!!,
                        amplitudes = amplitudes,
                        onProgressChange = { waveformProgress = it },
                        onProgressChangeFinished = { mediaController.seekTo((totalDuration * waveformProgress!!).toLong()) }
                    )

                    val containerWidth = with(density){ containerSize.width.toDp() }

                    DraggableBar(
                        modifier = modifier,
                        initialX = DRAGGABLE_BAR_THICKNESS,
                        minimumValue = DRAGGABLE_BAR_THICKNESS,
                        maximumValue = containerWidth
                    )

                    DraggableBar(
                        modifier = modifier,
                        initialX = containerWidth - DRAGGABLE_BAR_THICKNESS,
                        minimumValue = DRAGGABLE_BAR_THICKNESS,
                        maximumValue = containerWidth - DRAGGABLE_BAR_THICKNESS
                    )
                }


                val isPlaying by  rememberMediaControllerIsPlaying(mediaController = mediaController){
                    // Change the state of the app MediaController so that it does not overlap with this one
                    if(it) appState.mediaController?.pause() else appState.mediaController?.play()
                }

                Box(
                    modifier = modifier.padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center,
                    content = {
                        AnimatedAmplitudes(
                            modifier = Modifier.matchParentSize(),
                            barWidth = 3.dp,
                            gapWidth = 2.dp,
                            isAnimating = isPlaying,
                        )

                        PlayPauseButton(mediaController = mediaController)
                    }
                )
            }

            /*

            Column {
                // TOO : Check why doesn't this show as
                // TDO : SEEK TO WaVE PROGERESS DEFEing on the thing
                // TDO : cuttable

                val coroutineScope = rememberCoroutineScope()
                // TO : Update this somehow
                val showDurationMarker by remember { mutableStateOf(true) }

                val modifier = Modifier.fillMaxWidth()
                val colorScheme = MaterialTheme.colorScheme

                Canvas(modifier = modifier.height(48.dp)){
                    val width = size.width
                    val height = size.height

                    val lineWidth = 8f
                    drawLine(
                        color = colorScheme.primary,
                        strokeWidth = lineWidth,
                        start = Offset(x = 0f,y = 0f),
                        end =   Offset(x = 0f,y= height)
                    )

                    drawRoundRect(
                        color = colorScheme.primary,
                        topLeft = size.center.copy(x = 0f),
                        size = Size(width = lineWidth * 2,height = height / 3)
                    )

                    drawLine(
                        color = colorScheme.primary,
                        strokeWidth = lineWidth,
                        start = Offset(x = width,y = 0f),
                        end =   Offset(x = width, y= height)
                    )
                }

                // T : Finsih the draggable bars , with custom duration etc and then finish this screen
                // TO : Let user input custom range , to save that one
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    val currentPosition by mediaController.currentMediaItemPositionAsFlow(coroutineScope).collectAsState()

                    Text(
                        text = currentPosition,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    val duration by rememberMediaItemDuration(mediaController)

                    Text(
                        text = duration.formatAsTime(),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }*/
        },
        confirmButton = {
            // As this should be updated even if the screen is removed
            val coroutineScope = LocalAppState.current.viewModelScope

            TextButton(
                onClick = {
                    coroutineScope.launch {
                        // TODO : Respect custom start , end position of track by user
                        val values = ContentValues().apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                put(MediaStore.Audio.Media.GENRE, details.genre)
                            }

                            put(MediaStore.MediaColumns.RELATIVE_PATH, track.sourcePath)
                            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
                            put(MediaStore.MediaColumns.TITLE,details.name)
                            put(MediaStore.Audio.Media.ARTIST, details.artists)
                            put(MediaStore.Audio.Media.ALBUM, details.album)
                            put(MediaStore.Audio.Media.ALBUM_ARTIST, details.albumArtists)
                            put(MediaStore.Audio.Media.IS_RINGTONE, true)
                            put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
                            put(MediaStore.Audio.Media.IS_ALARM, true)
                            put(MediaStore.Audio.Media.IS_MUSIC, false)
                        }

                        val trackUri = Uri.parse(track.sourcePath)

                        val ringtoneContent = context.contentResolver.insert(trackUri,values)

                        RingtoneManager.setActualDefaultRingtoneUri(
                            context,
                            RingtoneManager.TYPE_RINGTONE,
                            ringtoneContent
                        )
                    }
                },
                content = {
                    Text(
                        text = stringResource(id = R.string.set_as_ringtone),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
    )
}

private val DRAGGABLE_BAR_THICKNESS = 4.dp
@Composable
private fun DraggableBar(
    modifier : Modifier = Modifier,
    initialX : Dp,
    minimumValue: Dp,
    maximumValue: Dp
) {
    var offsetX by remember { mutableStateOf(initialX) }
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .offset(x = offsetX)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX =
                            (offsetX.value + dragAmount).dp.coerceIn(minimumValue, maximumValue)
                    }
                )
            },
        content = {
            val color = MaterialTheme.colorScheme.primary

            Canvas(Modifier.height(DRAGGABLE_BAR_THICKNESS)) {
                val canvasHeight = size.height

                drawLine(
                    color = color,
                    cap = StrokeCap.Round,
                    strokeWidth = DRAGGABLE_BAR_THICKNESS.toPx(),
                    start = Offset(DRAGGABLE_BAR_THICKNESS.toPx() / 2, 0f),
                    end = Offset(DRAGGABLE_BAR_THICKNESS.toPx() / 2, canvasHeight),
                )

                val rectangleSize = Size(width = LINE_WIDTH * 2,height = canvasHeight / 3)
                val rectangleTopLeftY = size.center.y - canvasHeight / 6
                val rectangleCornerRadius = CornerRadius(x = 16f,y=16f)

                drawRoundRect(
                    color = color,
                    topLeft = size.center.copy(x = offsetX.toPx() - LINE_WIDTH,y = rectangleTopLeftY),
                    cornerRadius = rectangleCornerRadius,
                    size = rectangleSize
                )
            }

            // TODO : Complete this -> speech bubble with animated size content date picker inside
            AnimatedVisibility(visible = !isDragging) {
                Card {
                    Text("duration")
                }
            }
        }
    )
}