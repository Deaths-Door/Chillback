package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.chillback.core.layout.extensions.applyOn
import com.deathsdoor.chillback.core.media.components.TrackArtwork

@Composable
internal fun ColumnScope.AnimatedMediaItemCover(mediaItem: AstroMediaItem) = Row(verticalAlignment = Alignment.Bottom) {
    val size = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        size.animateTo(targetValue = 1f)
    }

    if(!size.isRunning) Spacer(modifier = Modifier.weight(10f))

    TrackArtwork(
        modifier = Modifier.animateContentSize()
            .apply {
                with(this@AnimatedMediaItemCover) {
                    weight(size.value)
                }
            }
            .applyOn(!size.isRunning) {
                padding(end = 16.dp)
            },
        mediaItem = mediaItem,
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = mediaItem.metadata?.displayTitle ?: mediaItem.metadata?.title ?: "Unknown",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = mediaItem.metadata?.artist ?: "Unknown",
            fontWeight = FontWeight.Bold,
            // For `Disabled` Effect
            color = LocalContentColor.current.copy(0.38f)
        )
    }
}