package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle

@Composable
fun SearchTrackOnlineThumbItem(track : Track,details : TrackDetails){
    val label = stringResource(R.string.find_song_elsewhere)
    val isOpen = remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable(onClickLabel = label) { isOpen.value = true }
            .optionsItemSpacing(),
        title = { ThumbnailTitle(text = label) },
        artwork = {
            Icon(
                imageVector= Icons.Default.Search,
                contentDescription = null
            )
        }
    )

    if(!isOpen.value)

    SearchTrackOnlineAlertDialog(isOpen = isOpen, track = track, details = details)
}

@Composable
fun SearchTrackOnlineDropDownItem(track : Track,details : TrackDetails?){
    val label = stringResource(R.string.find_song_elsewhere)
    val isOpen = remember { mutableStateOf(false) }

    DropdownMenuItem(
        text = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector= Icons.Default.Search,
                contentDescription = null
            )
        },
        onClick = { isOpen.value = true }
    )

    if(!isOpen.value)

    SearchTrackOnlineAlertDialog(isOpen = isOpen, track = track, details = details!!)
}



@Composable
private fun SearchTrackOnlineAlertDialog(
    isOpen : MutableState<Boolean>,
    track : Track,
    details : TrackDetails
) = AlertDialog(
    onDismissRequest = { isOpen.value = false },
    title = {
            
    },
    text = {

         // Search music video || lyric video || download if possible
    },
    confirmButton = {
        
    }
)