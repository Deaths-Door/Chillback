package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewModelScope
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.extensions.asMediaItems
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch

@Composable
fun AddToQueueThumbItem(tracks : Collection<Track>) {
    val appState = LocalAppState.current
    Thumbnail(
        title = "Add to queue",
        modifier = Modifier.clickable {
            appState.viewModelScope.launch {
                appState.mediaController?.addMediaItems(tracks.asMediaItems(appState.musicRepository))
            }
        }.optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.add_to_queue),
                contentDescription = null,
            )
        }
    )
}