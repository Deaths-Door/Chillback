package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import com.deathsdoor.chillback.ui.components.action.AddToQueueThumbItem
import com.deathsdoor.chillback.ui.components.action.DeleteTrackCollectionThumbItem
import com.deathsdoor.chillback.ui.components.action.optionsItemSpacing
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptions
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState

@Composable
fun TrackCollectionExtraOptions(
    state : ModalOptionsState,
    header : @Composable () -> Unit,
    trackCollection : TrackCollectionWithTracks,
    repository: TrackCollectionRepository
) = ModalOptions(
    state = state,
    dragHandle = null,
    content = {
        header()
        
        // TODO :  EDIT -> rename -> artwork change??
        // TODO:  add tracks to playlist
        AddToQueueThumbItem(tracks = trackCollection.tracks)

        Thumbnail(
            modifier = Modifier.clickable { /*TODO : SHARE IT*/ }.optionsItemSpacing(),
            title = "Share",
            artwork = {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
        )

        if(repository.isUserDefined) DeleteTrackCollectionThumbItem(
            trackCollection = trackCollection.collection
        )
    }
)