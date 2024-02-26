package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.ThumbnailWithText
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton

@Composable
fun TrackItem(
    modifier : Modifier = Modifier,
    track : Track,
    details : TrackDetails?,
    isSingleItemPerRow : Boolean,
    onLikeChange : (Boolean) -> Unit
) {
    val artists =  details?.artistWithOthers()
    val name = details?.name ?: "Wait a second"
    if(isSingleItemPerRow) ThumbnailWithText(
        modifier = modifier.fillMaxWidth(),
        artworkModifier = Modifier.size(64.dp),
        uri = details?.artwork,
        title = name,
        subtitle = artists,
        leadingIcon = {
            LikeButton(
                modifier = Modifier.padding(end = 12.dp),
                isLiked = track.isFavorite,
                onValueChange = onLikeChange
            )
        }
    ) else {
        Card(modifier = modifier){
            Box(modifier = modifier.fillMaxSize()) {
                Artwork(uri = details?.artwork)

                LikeButton(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp,bottom = 12.dp),
                    isLiked = track.isFavorite,
                    onValueChange = onLikeChange
                )
            }

           }
        }
        /*Card(modifier = modifier) {
            Box(modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(1f)) {
                Artwork(uri = details?.artwork)

                LikeButton(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp,bottom = 12.dp),
                    isLiked = track.isFavorite,
                    onValueChange = onLikeChange
                )
            }

            val basicMarquee = Modifier.basicMarquee()

            // From ThumbnailLayouts.kt
            Text(
                modifier = basicMarquee,
                text = name,
                style = MaterialTheme.typography.bodyMedium,
            )

            // From ThumbnailLayouts.kt
            artists?.let {
                Text(
                    modifier = basicMarquee,
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }*/
    }
}