package com.deathsdoor.chillback.ui.components.mediaplayer

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.deathsdoor.chillback.data.extensions.isLiked
import com.deathsdoor.chillback.data.extensions.mediaItemOfOrNull
import com.deathsdoor.chillback.data.extensions.setIsFavourite
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Deprecated("Do not use")
@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    track : Track
){
    var isCurrentlyLiked by remember(track.isFavorite) {  mutableStateOf(track.isFavorite) }

    val mediaController = LocalAppState.current.mediaController
    LikeButtonLogicWrapper(
        modifier = modifier,
        enabled = enabled,
        isCurrentlyLiked = isCurrentlyLiked,
        onValueChange = {
            // Update the UI immediately
            isCurrentlyLiked = it

            mediaController?.mediaItemOfOrNull(track) { index , mediaItem ->
                mediaController.updateMediaItemFavoriteStatus(
                    index = index,
                    mediaItem = mediaItem,
                    isCurrentlyLiked = isCurrentlyLiked
                )
            }
        },
        trackId = { track.id }
    )
}
@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    mediaController : Player,
    currentMediaItem : MediaItem,
) {
    var isCurrentlyLiked by remember(currentMediaItem.mediaMetadata) { mutableStateOf(currentMediaItem.isLiked()) }

    LikeButtonLogicWrapper(
        modifier = modifier,
        enabled = enabled,
        isCurrentlyLiked = isCurrentlyLiked,
        onValueChange = {
            isCurrentlyLiked = it

            mediaController.updateMediaItemFavoriteStatus(
                index = mediaController.currentMediaItemIndex,
                mediaItem = currentMediaItem,
                isCurrentlyLiked = it
            )
        },
        trackId = { currentMediaItem.mediaId.toLong() }
    )
}

private fun Player.updateMediaItemFavoriteStatus(index : Int,mediaItem : MediaItem,isCurrentlyLiked: Boolean) {
    val updatedMetadata = mediaItem.mediaMetadata.buildUpon().setIsFavourite(isCurrentlyLiked).build()
    val updatedMediaItem = mediaItem.buildUpon().setMediaMetadata(updatedMetadata).build()
    replaceMediaItem(index,updatedMediaItem)
}

@Composable
private fun LikeButtonLogicWrapper(
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    isCurrentlyLiked : Boolean,
    onValueChange: (Boolean) -> Unit,
    trackId : () -> Long
) {
    // Job updating the database with the information
    var pendingJob : Job? = remember { null }
    // Contains to which favorite status is the job changing the track to
    // Used to check if another task should be started (if jobType != isLiked)
    var jobType : Boolean = remember { false }

    val appState = if(LocalInspectionMode.current) null else LocalAppState.current
    val coroutineScope = appState?.viewModelScope
    val userRepository = appState?.userRepository

    IconToggleButton(
        modifier = modifier,
        enabled = enabled,
        checked = isCurrentlyLiked,
        content = {
            if (isCurrentlyLiked) Icon(
                imageVector = Icons.Filled.Favorite,
                tint = MaterialTheme.colorScheme.onError,
                contentDescription = "Current Song is Liked",
            )
            else Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Current Song is disliked",
            )
        },
        onCheckedChange = {
            // Update the UI immediately
            onValueChange(it)

            // If job is running and its setting isFavorite to [it] then don't start another job
            if(jobType == it && pendingJob != null) return@IconToggleButton

            Log.d("job","Launching isFavorite status change job")

            // Update it
            jobType = it

            // Cancel and negate the old job
            pendingJob?.cancel()
            pendingJob = null

            // Launch new job
            pendingJob = coroutineScope?.launch {
                delay(7500L)

                // If Value remains same after 5 seconds , update it in the database
                if(jobType == isCurrentlyLiked) {
                    // TODO : Check why am I getting 20x the Log message below where for all jobtype == true , it should only be running only , when rapidly clicking the button 40x -> answer all of them delays are ending around the same time , and maybe deplayes cuase them not to be cancled , and maybe then check if this job is pendingJob then only do it
                    Log.d("job","actually changing it to $jobType")

                    val id = trackId()
                    userRepository?.changeFavouriteStatusForTrack(id,jobType)
                } else Log.d("job","new value != jobtype")

                // TODO : MAYBE LAUNCH ANOTHER JOB IF NOT SAME
                pendingJob = null
            }
        }
    )
}