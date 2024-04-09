package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.runtime.Composable
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
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
        
        /*// TODO :  EDIT -> rename -> artwork change??
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
        )*/
    }
)