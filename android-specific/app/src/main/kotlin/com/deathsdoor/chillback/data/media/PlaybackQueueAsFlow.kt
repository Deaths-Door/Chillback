package com.deathsdoor.chillback.data.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.media3.common.Player
import androidx.media3.common.Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED
import androidx.media3.common.Player.TimelineChangeReason
import androidx.media3.common.Timeline
import com.deathsdoor.chillback.data.extensions.mapMediaItems
import com.deathsdoor.chillback.data.models.TrackDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlaybackQueueAsFlow {
    private val mutableFlow : MutableStateFlow<List<TrackDetails>> = MutableStateFlow(emptyList())
    private var listener : Player.Listener? = null

    @Composable
    fun observe(coroutineScope: CoroutineScope,mediaController: Player): StateFlow<List<TrackDetails>> {
        var stateFlow : StateFlow<List<TrackDetails>> = mutableFlow

        LaunchedEffect(Unit) {
            stateFlow = mutableFlow.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )


            if(listener == null) return@LaunchedEffect

            listener = object : Player.Listener {
                override fun onTimelineChanged(timeline: Timeline, reason: @TimelineChangeReason Int) {
                    super.onTimelineChanged(timeline, reason)
                    if(reason == TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED) mutableFlow.tryEmit(mediaController.mapMediaItems { TrackDetails.from(it) })
                }
            }

            mediaController.addListener(listener!!)
        }

        DisposableEffect(Unit) {
            onDispose {
                listener?.let { mediaController.removeListener(it) }
            }
        }

        return stateFlow
    }
}