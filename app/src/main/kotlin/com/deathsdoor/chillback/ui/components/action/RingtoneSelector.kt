package com.deathsdoor.chillback.ui.components.action

import android.content.ContentValues
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.mediaplayer.AnimatedDuration
import com.deathsdoor.chillback.ui.components.mediaplayer.currentMediaItemPositionAsFlow
import com.deathsdoor.chillback.ui.components.mediaplayer.formatAsTime
import com.deathsdoor.chillback.ui.components.mediaplayer.rememberMediaItemDuration
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import kotlinx.coroutines.launch

//TODO:Option to select default ringtone from device https://stackoverflow.com/questions/2724871/how-to-bring-up-list-of-available-notification-sounds-on-android
@Composable
fun RingtoneSelectorThumbItem(track: Track, details : TrackDetails){
    val label = "Set As Ringtone"
    var isDialogOpen by remember { mutableStateOf(false) }
    Thumbnail(
        modifier = Modifier
            .clickable(onClickLabel = label) {
                isDialogOpen = true
            }
            .optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.media3_notification_play),
                contentDescription = label
            )
        },
        title = label
    )

    if(!isDialogOpen) return

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { isDialogOpen = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text("Set As Ringtone") },
        text = {
            val snackbarStateError = LocalErrorSnackbarState.current
            val appState = LocalAppState.current
            val musicRepository = appState.musicRepository

            var waveformProgress by remember { mutableStateOf<Float?>(null) }
            val amplitudes = mutableStateListOf<Int>()
            var totalDuration by remember { mutableStateOf(-1L) }
            val mediaController = remember {
                ExoPlayer.Builder(context).build().apply { prepare() }
            }

            DisposableEffect(Unit) {
                onDispose { mediaController.release() }
            }

            LaunchedEffect(Unit) {
               /* val amplituda = Amplituda(context)
                val audioData = amplituda.processAudio(track.sourcePath, Cache.withParams(Cache.REUSE))
                    .get(AmplitudaErrorListener {
                        runBlocking {
                            snackbarStateError.showSnackbar("Unable to open file. Please try again.\n Cause is ${it.message}")
                        }
                    })

                totalDuration = audioData.getAudioDuration(AmplitudaResult.DurationUnit.MILLIS)

                val _amplitudes = audioData.amplitudesAsList()
                Log.d("waveform","duration = $totalDuration\namplitudes=${_amplitudes}")
                amplitudes.addAll(_amplitudes)*/
                waveformProgress = 0F
                mediaController.addMediaItem(track.asMediaItem(musicRepository = musicRepository))
            }

            if(waveformProgress == null) {
                Text("Wait a second .. Fetching data!")
                return@AlertDialog
            }


            LaunchedEffect(waveformProgress) {
                Log.d("waveform","duration=${totalDuration}\nprogress -> $waveformProgress\n${totalDuration * waveformProgress!!}")
            }

            Column {
                // TODO : Check why doesn't this show as
                // TODO : SEEK TO WaVE PROGERESS DEFEing on the thing
                // TODO : cuttable

                val coroutineScope = rememberCoroutineScope()
                // TODO : Update this somehow
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

                // TODO : Finsih the draggable bars , with custom duration etc and then finish this screen
                AnimatedDuration(
                    modifier = modifier,
                    mediaController = mediaController,
                  //  coroutineScope = coroutineScope,
                  //  showDurationMarker = showDurationMarker,
                    onPlayChanged = { if(it) appState.mediaController?.pause() else appState.mediaController?.play() },
                    /*content = {
                        // TODO : Check why this works without the if text above and collapses on click
                        AudioWaveform(
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


                    }*/
                )

                // TODO : Let user input custom range , to save that one
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
            }
        },
        confirmButton = {
            // As this should be updated even if the screen is removed
            val coroutineScope = LocalAppState.current.viewModelScope

            TextButton(
                onClick = {
                    coroutineScope.launch {
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
                        text = "Set Ringtone",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
    )
}

private const val LINE_WIDTH = 8f

@Composable
private fun DraggableBars(modifier : Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    var offsetLeftX by remember { mutableStateOf(LINE_WIDTH) }
    var offsetRightX : Float? by remember { mutableStateOf(null) }

    var isLeftSideBarSelected by remember { mutableStateOf(true) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
               /*detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        sidebarPaths.forEachIndexed { index, it ->
                            val difference = Path.combine(
                                operation = PathOperation.Difference,
                                path1 = touchPath,
                                path2 = it
                            )

                            if(difference.isEmpty) touchedIndex = index
                        }
                    },
                    onDragCancel = { touchedIndex = -1 },
                    onDragEnd = { touchedIndex = -1 },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()

                        val pathData = sidebarPaths.getOrNull(touchedIndex)

                        pathData?.let {
                            val matrix = Matrix().apply {
                                postTranslate(dragAmount, 0f)
                            }

                            pathData.asAndroidPath().transform(matrix)
                        }

                        if(touchedIndex == 0) offsetLeftX += dragAmount
                        else offsetRightX = offsetRightX?.plus(dragAmount)
                    }
                )*/
            },
        onDraw = {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val rectangleSize = Size(width = LINE_WIDTH * 2,height = canvasHeight / 3)
            val rectangleTopLeftY = size.center.y - canvasHeight / 6
            val rectangleCornerRadius = CornerRadius(x = 16f,y=16f)

            val coercedOffSetLeftX = offsetLeftX.coerceIn(LINE_WIDTH,canvasWidth)
            if(offsetRightX == null) offsetRightX = canvasWidth - LINE_WIDTH
            val coercedOffSetRightX = offsetRightX!!.coerceIn(LINE_WIDTH,canvasWidth - LINE_WIDTH)

            drawLine(
                color = colorScheme.primary,
                strokeWidth = LINE_WIDTH,
                cap = StrokeCap.Round,
                start = Offset(x = coercedOffSetLeftX,y = 0f),
                end =  Offset(x = coercedOffSetLeftX,y= canvasHeight)
            )

            drawRoundRect(
                color = colorScheme.primary,
                topLeft = size.center.copy(x = coercedOffSetLeftX - LINE_WIDTH,y = rectangleTopLeftY),
                cornerRadius = rectangleCornerRadius,
                size = rectangleSize
            )

            drawLine(
                color = colorScheme.primary,
                strokeWidth = LINE_WIDTH,
                cap = StrokeCap.Round,
                start = Offset(x = coercedOffSetRightX,y = 0f),
                end =   Offset(x = coercedOffSetRightX, y= canvasHeight)
            )

            drawRoundRect(
                color = colorScheme.primary,
                topLeft = size.center.copy(x = coercedOffSetRightX - LINE_WIDTH,y = rectangleTopLeftY),
                cornerRadius = rectangleCornerRadius,
                size = rectangleSize
            )
        }
    )
}

@Preview(name = "DraggableBar", backgroundColor = 0x00F44336)
@PreviewDynamicColors
@PreviewLightDark
@PreviewScreenSizes
@PreviewFontScale
@Composable
internal fun DraggableBarPreview() = DraggableBars(
    Modifier
        .width(128.dp)
        .height(48.dp)
        .padding(vertical = 4.dp)
)