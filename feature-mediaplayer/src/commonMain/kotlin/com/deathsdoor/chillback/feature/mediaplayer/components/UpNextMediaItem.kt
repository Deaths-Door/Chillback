package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

@Composable
internal fun UpNextMediaItem(
    mediaItem: AstroMediaItem,
    modifier : Modifier = Modifier,
) = ElevatedCard(modifier) {
    TrackArtwork(mediaItem = mediaItem,modifier = Modifier.fillMaxHeight().aspectRatio(1f))

    val _8dp = 8.dp
    val _12dp = 12.dp

    Column(modifier = Modifier.padding(top = _8dp,bottom = _8dp,start = _12dp,end = _12dp * 3)) {
        Text(
            text = stringResource(Res.strings.up_next),
            style = MaterialTheme.typography.bodyLarge,
        )

        val metadata = mediaItem.metadata

        Text(
            text = "${metadata?.displayTitle ?: metadata?.title} - ${metadata?.albumTitle ?: metadata?.genre} ○ ${metadata?.artist ?: metadata?.albumArtist}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}