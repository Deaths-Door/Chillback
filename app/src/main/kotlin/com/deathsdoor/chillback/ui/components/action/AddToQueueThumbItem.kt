package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddToQueueThumbItem(onAdd : suspend CoroutineScope.(MediaController?) -> Unit) {
    val appState = LocalAppState.current
    Thumbnail(
        title = "Add to queue",
        modifier = Modifier.clickable {
            appState.viewModelScope.launch { onAdd(appState.mediaController) }
        }.optionsItemSpacing(),
        artwork = {
            Icon(
                painter = painterResource(id = R.drawable.add_to_queue),
                contentDescription = null,
            )
        }
    )
}