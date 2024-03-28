package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.navigation.navigateToTrackMetadataScreen
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
@NonRestartableComposable
fun TrackMetadataThumbItem(track : Track,onClick : () -> Unit) {
    val navController = LocalAppState.current.navController
    val label = "Metadata"

    Thumbnail(
        modifier = Modifier.clickable(onClickLabel =  label) {
            onClick()
            navController.navigateToTrackMetadataScreen(track = track)
        }.optionsItemSpacing(),
        title = label,
        artwork = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = label
            )
        }
    )
}