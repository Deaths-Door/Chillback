package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import com.deathsdoor.chillback.ui.components.layout.ThumbnailWithText
import com.deathsdoor.chillback.ui.components.track.TrackSongCount
import com.deathsdoor.chillback.ui.extensions.themeBasedTint
import kotlinx.coroutines.delay

@Composable
fun TrackCollectionItem(
    modifier : Modifier = Modifier,
    trackCollection: TrackCollectionWithTracks
) = ThumbnailWithText(
    modifier = modifier.fillMaxWidth(),
    uri = trackCollection.collection.imageSource,
    title = trackCollection.collection.name,
    // artworkModifier => Modifier.padding(top = 8.dp).size(16.dp)
    // imageModifier => Modifier.size(72.dp)
    subtitle = null/*{ //artworkModifier ->
        /*if(trackCollection.collection.isPinned) {
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                TrackCollectionItemPushPinImage(
                    modifier = artworkModifier,
                    isPinned = trackCollection.collection.isPinned,
                )

                AnimatedTrackSongCount(trackCollection = trackCollection)
            }
        } else AnimatedTrackSongCount(trackCollection = trackCollection)*/
    }*/
)

@Composable
@NonRestartableComposable
fun TrackCollectionItemPushPinImage(modifier : Modifier = Modifier,isPinned : Boolean?) = Icon(
    modifier = modifier.rotate(45f),
    painter = painterResource(id = R.drawable.push_pin),
    contentDescription = if(isPinned == true) "Pin" else "Unpin",
    tint = themeBasedTint()
)

@Composable
private fun AnimatedTrackSongCount(trackCollection : TrackCollectionWithTracks) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(trackCollection.collection.isPinned) {
        isVisible = false
        delay(250)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { w -> w }) + expandHorizontally(expandFrom = Alignment.End),
        exit = slideOutHorizontally(targetOffsetX = { w -> w }) + shrinkHorizontally(shrinkTowards = Alignment.End),
        content = {
            TrackSongCount(
                modifier = Modifier.padding(end = 16.dp),
                count = trackCollection.tracks,
                style = MaterialTheme.typography.labelMedium
            )
        }
    )
}