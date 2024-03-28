package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.ThumbnailWithText
import com.deathsdoor.chillback.ui.extensions.fadedRightEdge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@NonRestartableComposable
fun TrackSmuggedArtworkCard(
    modifier : Modifier = Modifier,
    details : TrackDetails,
    onClick : () -> Unit
) = Card(
    modifier = modifier,
    onClick = onClick,
    content = {
        ThumbnailWithText(
            artworkModifier = Modifier.size(90.dp).fadedRightEdge(),
            uri = details.artwork,
            title = details.name,
            subtitle = details.artistWithOthers()
        )
    }
)