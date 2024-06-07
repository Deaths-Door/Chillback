package com.deathsdoor.chillback.feature.mediaplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.feature.mediaplayer.components.DurationSlider
import com.deathsdoor.chillback.feature.mediaplayer.components.FullScreenMediaPlayerButton
import com.deathsdoor.chillback.feature.mediaplayer.components.HorizontalDurationMarkers
import com.deathsdoor.chillback.feature.mediaplayer.components.MainMediaActionRow
import com.deathsdoor.chillback.feature.mediaplayer.components.SecondaryMediaActionRow
import com.deathsdoor.chillback.core.media.components.TrackArtwork
import com.deathsdoor.chillback.core.media.components.VerticalMediaItemTitleWithArtists

@Composable
fun LargeMediaPlayerBar(
    state: AstroPlayerState,
    navController: NavController,
    applicationNavController: NavController,
    modifier : Modifier = Modifier
) = MusicPlayerShared(state){
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        TrackArtwork(
            modifier = Modifier.size(128.dp),
            mediaItem = state.currentMediaItem!!
        )

        VerticalMediaItemTitleWithArtists(
            modifier = Modifier.padding(start = 8.dp),
            mediaItem = state.currentMediaItem!!,
            titleStyle = MaterialTheme.typography.headlineLarge,
            artistStyle = MaterialTheme.typography.bodyLarge
        )

        val iconSize = 52.dp
        val iconSizeModifier = Modifier.size(iconSize)

        LikeButton(
            modifier = iconSizeModifier,
            astroPlayer = state.astroPlayer,
            mediaItem = state.currentMediaItem!!
        )

        val _05Weight =Modifier.weight(0.5f)

        Spacer(modifier = _05Weight)

        Column(modifier = Modifier.weight(5f)) {
            val maxWidth = Modifier.fillMaxWidth()

            MainMediaActionRow(
                modifier = maxWidth,
                state = state,
                iconSize = iconSize
            )

            DurationSlider(
                modifier = maxWidth,
                state = state,
            )

            HorizontalDurationMarkers(
                modifier = maxWidth,
                state = state,
            )
        }

        Spacer(modifier = _05Weight)

        SecondaryMediaActionRow(
            state = state,
            iconSize =  iconSize,
            navController = applicationNavController
        )

        FullScreenMediaPlayerButton(
            modifier = iconSizeModifier,
            navController = navController
        )
    }
}