package com.deathsdoor.chillback.data.mediaplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlin.text.*
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun rememberMediaItemDuration(mediaController: MediaController) = remember(mediaController.duration) {
    val mediaDuration = mediaController.duration

    // This should not be needed , but is there for safely
    // Since mediaController.duration defaults to C.TIME_UNSET when no duration given , which is Long.MIN_VALUE + 1
    val durationAfterCorrection = if(mediaDuration == C.TIME_UNSET) 1 else mediaDuration
    mutableStateOf(durationAfterCorrection)
}


fun MediaController?.currentMediaItemAsFlow(scope: CoroutineScope) : SharedFlow<MediaItem> = flow {
    while (true) {
        val mediaItem = (this@currentMediaItemAsFlow?.currentMediaItem ?: return@flow)
        emit(mediaItem)

        delay(1000) // Update the media metadata every second
    }
}.shareIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000L)
)

fun MediaController.currentMediaItemPositionAsFlow(scope: CoroutineScope) : SharedFlow<Long> = flow {
    while (true) {
        emit(this@currentMediaItemPositionAsFlow.currentPosition)

        delay(1000) // Update the media metadata every second
    }
}.shareIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000L)
)

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SharedFlow<Long>.collectFormatAsTimeAsState() : State<String> =
    mapLatest { it.formatAsTime() }
    .collectAsState(formatIntoTime(0,0,0))

fun Long.formatAsTime() = milliseconds.toComponents { hours, minutes, seconds, _ -> formatIntoTime(hours,minutes,seconds) }

private fun formatIntoTime(hours : Long, minutes : Int, seconds : Int): String = String.format(
    "%02d:%02d:%02d",
    hours,
    minutes,
    seconds,
)
