package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.track.Artwork
import com.deathsdoor.chillback.ui.extensions.themeBasedTint
import kotlinx.coroutines.delay

// TODO : Finish this implementation first
// TODO : Add MultiSelect Visual Support + Accessibilit  https://medium.com/androiddevelopers/create-a-photo-grid-with-multiselect-behavior-using-jetpack-compose-9a8d588a9b63
@Composable
fun TrackCollectionItem(
    modifier : Modifier = Modifier,
    isSingleItemPerRow : Boolean,
    collection: TrackCollection
){
    val name = collection.name
    val uri = collection.imageSource

    if(isSingleItemPerRow) Thumbnail(
        modifier = modifier.fillMaxWidth(),
        artwork = {
            Artwork(
                modifier = Modifier.size(64.dp),
                uri = uri
            )
        },
        title = name,
        subtitle = {
            // TODO : Implement this shit
            /*if(collection.isPinned) {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    TrackCollectionItemPushPinImage(
                        modifier = artworkModifier,
                        isPinned = collection.isPinned,
                    )

                    AnimatedTrackSongCount(trackCollection = trackCollection)
                }
            } else AnimatedTrackSongCount(trackCollection = trackCollection)*/
        }
        //subtitle = artists,
        /*leadingIcon = {
            LikeButton(
                modifier = Modifier.padding(end = 12.dp),
                isLiked = track.isFavorite,
                onValueChange = onLikeChange
            )
        }*/
    ) else Card(modifier = modifier.aspectRatio(1f)){
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Artwork(
                modifier= Modifier.matchParentSize(),
                uri = uri,
                contentScale = ContentScale.FillBounds
            )

            /*LikeButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                isLiked = track.isFavorite,
                onValueChange = onLikeChange
            )*/
        }

        val basicMarquee = Modifier.basicMarquee().padding(horizontal = 8.dp)

        // From ThumbnailLayouts.kt
        Text(
            modifier = basicMarquee,
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )

        // From ThumbnailLayouts.kt
        /*artists?.let {
            Text(
                modifier = basicMarquee,
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
        }*/

        Spacer(modifier = Modifier.height(8.dp))
    }
} /*ThumbnailWithText(
    modifier = modifier.fillMaxWidth(),
    uri = collection.imageSource,
    title = collection.name,
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
*/
@Composable
@NonRestartableComposable
fun TrackCollectionItemPushPinImage(modifier : Modifier = Modifier,isPinned : Boolean?) = Icon(
    modifier = modifier.rotate(45f),
    painter = painterResource(id = R.drawable.push_pin),
    contentDescription = if(isPinned == true) "Pin" else "Unpin",
    tint = themeBasedTint()
)

@Composable
private fun AnimatedTrackSongCount(collection: TrackCollection) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(collection.isPinned) {
        isVisible = false
        delay(250)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { w -> w }) + expandHorizontally(expandFrom = Alignment.End),
        exit = slideOutHorizontally(targetOffsetX = { w -> w }) + shrinkHorizontally(shrinkTowards = Alignment.End),
        content = {
            /*TrackSongCount(
                modifier = Modifier.padding(end = 16.dp),
                count = tracks,
                style = MaterialTheme.typography.labelMedium
            )*/
        }
    )
}