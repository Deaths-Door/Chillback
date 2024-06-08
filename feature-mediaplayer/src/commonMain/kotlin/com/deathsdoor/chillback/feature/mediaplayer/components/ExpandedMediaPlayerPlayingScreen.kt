package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.components.VerticalMediaItemTitleWithArtists

@Composable
internal fun ExpandedMediaPlayerPlayingScreenPortrait(
    state : AstroPlayerState,
    navController : NavController
) {
    val modifier = Modifier.padding(16.dp)
    val trackArtworkModifier = modifier.aspectRatio(1f)

    val iconSize = 72.dp
    val iconSizeModifier = Modifier.size(iconSize)

    Column(modifier) {
        TrackArtwork(
            modifier = trackArtworkModifier.fillMaxWidth(),
            mediaItem = state.currentMediaItem!!
        )

        Row {
            VerticalMediaItemTitleWithArtists(
                modifier = Modifier.weight(1f),
                mediaItem = state.currentMediaItem!!,
                titleStyle = MaterialTheme.typography.displaySmall,
                artistStyle = MaterialTheme.typography.titleMedium
            )

            LikeButton(
                modifier = iconSizeModifier,
                astroPlayer = state.astroPlayer,
                mediaItem = state.currentMediaItem!!
            )
        }

        DurationSlider(state)

        HorizontalDurationMarkers(
            modifier = Modifier.fillMaxWidth(),
            state = state,
        )

        MainMediaActionRow(state)

        Row {
            SecondaryMediaActionRow(
                state = state,
                iconSize = iconSize,
                navController = navController
            )
        }
    }
}