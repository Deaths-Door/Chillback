package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.extensions.orReport
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddToQueueThumbItem(onAdd : suspend CoroutineScope.(MediaController?) -> Unit) {
    val appState = LocalAppState.current
    val label = stringResource(R.string.add_to_queue)

    Thumbnail(
        title = { ThumbnailTitle(text = label) },
        modifier = Modifier
            .clickable(onClickLabel = label) {
                appState.viewModelScope.launch { onAdd(appState.mediaController) }
            }
            .optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.add_to_queue),
                contentDescription = label,
            )
        }
    )
}

@Composable
fun AddToQueueDropDownItem(onAdd : suspend CoroutineScope.(MediaController?) -> Unit) {
    val appState = LocalAppState.current
    val label = stringResource(R.string.add_to_queue)
    DropdownMenuItem(
        text = { Text(label) },
        onClick = {
            appState.viewModelScope.launch { onAdd(appState.mediaController) }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.add_to_queue),
                contentDescription = label,
            )
        }
    )
}

@Composable
fun AddTrackToQueueShared(track : Track,content : @Composable (onAdd : suspend CoroutineScope.(MediaController?) -> Unit) -> Unit) {
    val musicRepository = LocalAppState.current.musicRepository
    val snackbarState = LocalSnackbarState.current

    content { mediaController ->
        track.asMediaItem(musicRepository).orReport(snackbarState) {
            mediaController?.addMediaItem(it)
        }
    }
}