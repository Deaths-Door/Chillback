package com.deathsdoor.chillback.ui.components.track

import androidx.compose.runtime.Composable
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.action.AddToQueueThumbItem
import com.deathsdoor.chillback.ui.components.action.DeleteThumbItem
import com.deathsdoor.chillback.ui.components.action.PlayNextThumbItem
import com.deathsdoor.chillback.ui.components.action.PlayNowThumbItem
import com.deathsdoor.chillback.ui.components.action.TrackMetadataThumbItem
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptions
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun TrackExtraOptions(
    state: ModalOptionsState,
    index : Int,
    track : Track,
    details : TrackDetails,
    tracks : List<Track>?,
    onRemove : ((index : Int,Track) -> Unit)? = null,
) = ModalOptions(
    state = state,
    content = {
        // TODO : Add Header item thingy -> Faviroute
        // Play Now
        // Play Next
        // Add to Playback Queue
        // Add to playlist -> TODO
        // Metadata and Edit-> TODO
        // Share -> TODO
        // Set As Ringtone -> TODO
        // Lyrics -> TODO
        // Search In Youtube/Other Services
        // Delete / Remove from list

        val appState = LocalAppState.current
        val mediaController = appState.mediaController

        PlayNowThumbItem(
            onClick = onTrackItemClick(
                mediaController = mediaController,
                appState = appState,
                index = index,
                track = track,
                tracks = tracks,
            )
        )

        PlayNextThumbItem(
            onClick = onTrackItemClick(
                mediaController = mediaController,
                appState = appState,
                index = index,
                track = track,
                tracks = tracks,
                addOnNext = true
            )
        )

        val musicRepository = appState.musicRepository

        AddToQueueThumbItem {
            it?.addMediaItem(track.asMediaItem(musicRepository))
        }

        TrackMetadataThumbItem(track = track) { state.dismiss() }

        onRemove?.let {
            DeleteThumbItem(
                label = "Remove Track From Collection",
                name = details.name,
                onDelete = { _ ->
                    it(index,track)
                }
            )
        }

        val userRepository = LocalAppState.current.userRepository

        DeleteThumbItem(
            label = "Delete Track from Device",
            name = details.name,
            onDelete = { coroutineScope ->
                coroutineScope.launch {
                    val job = userRepository.removeTrack(track)

                    val file = File(track.sourcePath)
                    file.delete()

                    job.join()
                }
            }
        )
    }
)