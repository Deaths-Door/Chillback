package com.deathsdoor.chillback.core.media.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroMediaMetadata
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.media.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    astroPlayer: AstroPlayer,
    mediaItem : AstroMediaItem,
    enabled : Boolean = true,
) {
    val isLiked by rememberMediaItemLikedState(mediaItem)

    LaunchedEffect(Unit) {
        snapshotFlow { isLiked }
            .distinctUntilChanged()
            .debounce(5000L)
            .mapLatest {
                // TODO - UPDATE BACKEND ABOUT LIKED STATUS
            }
    }

    IconToggleButton(
        modifier = modifier,
        checked = isLiked,
        enabled = enabled,
        content = {
            when(isLiked) {
                true -> Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = MaterialTheme.colorScheme.onError,
                    contentDescription = stringResource(Res.strings.mediaitem_is_liked),
                )
                false ->Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(Res.strings.mediaitem_is_not_liked),
                )
            }
        },
        onCheckedChange = { value ->
            // Update UI and Backend for Liked State Change
            // 1. Update UI Immediately
            // 2. Update Media Item and Playback Queue
            //    - Modify the "liked" property of the media item object.
            //    - If the media item is present in the playback queue:
            //      - Update the playback queue to reflect the new "liked" state.
            // 3.  Update Backend After Debounce Period which is 5 seconds (Handled by LaunchEffect Above)
            //    - This optimizes performance by avoiding unnecessary updates due to frequent changes.

            val updatedMediaItem = mediaItem.updateIsLiked(value)

            // Solution for indexOfFirstOrNull(predicate) -- https://github.com/JetBrains/kotlin/pull/4807
            astroPlayer.allMediaItems()
                .indexOfFirst { it == mediaItem }
                .takeUnless { it == -1 }?.let { index ->
                    astroPlayer.replaceMediaItem(index,updatedMediaItem)
                }
        }
    )
}


fun AstroMediaItem.isLiked() = metadata?.extras?.get("isLiked") as? Boolean ?: false
fun AstroMediaItem.updateIsLiked(value : Boolean) : AstroMediaItem {
    if(this.isLiked()) return this

    val metadata = this.metadata ?: AstroMediaMetadata()
    val extras = metadata.extras?.toMutableMap() ?: mutableMapOf()
    extras["isLiked"] = value

    return this.copy(metadata = metadata.copy(extras = extras))
}

@Composable
private fun rememberMediaItemLikedState(mediaItem: AstroMediaItem) = remember(mediaItem.metadata?.extras?.get("isLiked")) {
    mutableStateOf(mediaItem.metadata?.extras?.get("isLiked") as? Boolean ?: false)
}