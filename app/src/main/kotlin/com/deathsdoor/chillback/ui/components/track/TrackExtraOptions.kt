package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.repositories.UserRepository
import com.deathsdoor.chillback.ui.components.action.AddToQueueThumbItem
import com.deathsdoor.chillback.ui.components.action.DeleteThumbItem
import com.deathsdoor.chillback.ui.components.action.PlayNextThumbItem
import com.deathsdoor.chillback.ui.components.action.PlayNowThumbItem
import com.deathsdoor.chillback.ui.components.action.RingtoneSelectorThumbItem
import com.deathsdoor.chillback.ui.components.action.ShareThumbItem
import com.deathsdoor.chillback.ui.components.action.TrackMetadataThumbItem
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackExtraOptions(
    track : Track,
    details : TrackDetails,
    tracks : List<Track>?,
    onDismissRequest : () -> Unit,
    onRemove : ((Track) -> Unit)? = null,
) = ModalBottomSheet(
    onDismissRequest =  onDismissRequest,
    content = {

        // TODO : Add Header item thingy -> Faviroute

        // Keep this sync with TrackItem::MoreInfoButtonContent
        // Play Now
        // Play Next
        // Add to Playback Queue
        // Add to playlist -> TODO
        // Metadata and Edit
        // Share
        // Set As Ringtone
        // Lyrics -> TODO
        // Edit File (length etc) -> TODO
        // Search In Youtube/Other Services -> TODO
        // Delete / Remove from list

        val appState = LocalAppState.current
        val mediaController = appState.mediaController

        PlayNowThumbItem(
            onClick = {
                onTrackItemClick(
                    mediaController = mediaController,
                    appState = appState,
                    track = track,
                    tracks = tracks,
                )
            }
        )

        PlayNextThumbItem(
            onClick = {
                onTrackItemClick(
                    mediaController = mediaController,
                    appState = appState,
                    track = track,
                    tracks = tracks,
                    addOnNext = true
                )
            }
        )

        val musicRepository = appState.musicRepository

        AddToQueueThumbItem {
            it?.addMediaItem(track.asMediaItem(musicRepository))
        }

        TrackMetadataThumbItem(track = track, onClick = onDismissRequest)

        ShareThumbItem()

        RingtoneSelectorThumbItem(track = track,details = details)

        onRemove?.let {
            DeleteThumbItem(
                label = stringResource(R.string.remove_track_from_collection),
                name = details.name,
                onDelete = { _ ->
                    it(track)
                }
            )
        }

        val userRepository = LocalAppState.current.userRepository

        DeleteThumbItem(
            label = stringResource(R.string.delete_track_from_device),
            name = details.name,
            onDelete = { coroutineScope ->
                coroutineScope.trackOnDelete(userRepository, track)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
)

fun CoroutineScope.trackOnDelete(userRepository : UserRepository, track: Track) = launch {
    // Remove it form the database
    val job = userRepository.removeTrack(track)

    val file = File(track.sourcePath)
    file.delete()

    job.join()
}