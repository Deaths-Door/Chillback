package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.extensions.hasNotSameMediaItemsAs
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.repositories.MusicRepository
import com.deathsdoor.chillback.ui.components.action.LazyOptionsRow
import com.deathsdoor.chillback.ui.components.action.rememberIsSingleItemRow
import com.deathsdoor.chillback.ui.components.layout.LazyDismissibleList
import com.deathsdoor.chillback.ui.components.layout.rememberSelectedIDsOrNotInMultiSelectMode
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.state.ChillbackAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun LazyTrackList(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope,
    tracks: List<Track>?,
    onTracksSorted : (sorted : List<Track>) -> Unit,
    onRemove : ((index : Int,Track) -> Unit)? = null,
    placeHolderText : (@Composable () -> AnnotatedString)? = null
) = Column(modifier = modifier) {
    val isSingleItemPerRow = rememberIsSingleItemRow()
    val details = remember { mutableStateListOf<TrackDetails>() }

    val musicRepository = LocalAppState.current.musicRepository

    val selectedIDs = rememberSelectedIDsOrNotInMultiSelectMode()

    LazyOptionsRow(
        coroutineScope = coroutineScope,
        isSingleItemPerRow = isSingleItemPerRow,
        selectedIDs = selectedIDs,
        data = tracks,
        criteria = listOf("Name","Genre","Album","Album Artists","Artists","Has Artwork","Is Favourite"),
        fetch = { tracks!!.size != details.size },
        key = { it.id },
        onFetch = {
            details.subList(0,tracks!!.size).forEachIndexed { index, _ ->
                receiveTrackDetails(
                    musicRepository = musicRepository,
                    details = details,
                    track = tracks[index],
                    index = index,
                    run = { it() }
                )
            }
        },
        onSort = { isAscendingOrder , appliedMethods ->
            onTracksSorted(
                tracks!!.sortBasedOn(
                    isAscending = isAscendingOrder,
                    sortMethods = appliedMethods,
                    details = details
                )
            )
        }
    )

    LazyDismissibleList(
        items = tracks,
        key = { _, track -> track.id },
        isSingleItemPerRow = isSingleItemPerRow.value,
        selectedIDs = selectedIDs,
        confirmValueChange = { dismissValue, index ,track ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart && onRemove != null) {
                onRemove(index, track)
                return@LazyDismissibleList true
            }

            false
        },
        startToEndColor = Color(0xFFCCE5D6),
        endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
        swipeableContent = { isStartToEnd , track ->
            if(isStartToEnd) LikeButton(track = track,enabled = false)
            else Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        placeHolder = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    Text(
                        text = if(placeHolderText != null) placeHolderText() else {
                            styledText(
                                plain0 = "There are no playlists to display yet.\n",
                                colored0 = "Explore",
                                plain1 = " and discover some music ",
                                colored1 = "to fill your library!"
                            )
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // TODO : Add add tracks to playlist button here , only if user defined == false
                }
            )
        },
        // TODO : ADD PLAY ALL Option
        // TODO : ADD A SELECT ALL BOTTOM SHEET / FAB
        optionContent = { state,index, track ->
            TrackExtraOptions(
                state = state,
                index = index,
                track = track,
                tracks = tracks,
                details = details[index],
                onRemove = onRemove
            )
        },
        content = { index , track , isSelected , onLongClick ->
            val appState = LocalAppState.current
            val trackDetails = receiveTrackDetails(
                musicRepository = appState.musicRepository,
                details = details,
                track = track,
                index = index,
                run = { LaunchedEffect(Unit) { it() } }
            )

            val mediaController = appState.mediaController

            TrackItem(
                modifier = if(isSelected != null) Modifier
                else Modifier.combinedClickable(
                    onClick = onTrackItemClick(
                        mediaController = mediaController,
                        track = track,
                        tracks = tracks,
                        appState = appState,
                        index = index
                    ),
                    onClickLabel = "Play this track",
                    onLongClick = if(trackDetails != null) onLongClick else null,
                    onLongClickLabel = if(trackDetails != null) "Show extra options for track" else null,
                ),
                track = track,
                details = trackDetails,
                isSingleItemPerRow = isSingleItemPerRow.value,
                isSelected =  isSelected,
                selectedIDs = selectedIDs
            )
        }
    )
}

private inline fun receiveTrackDetails(
    details : SnapshotStateList<TrackDetails>,
    musicRepository: MusicRepository,
    track : Track,
    index : Int,
    run: (receiver : suspend () -> Unit) -> Unit
): TrackDetails? {
    val detail = details.getOrNull(index)
    if(detail == null) run {
        details.add(index,musicRepository.trackDetails(track))
    }
    return detail
}

fun onTrackItemClick(
    mediaController: MediaController?,
    track : Track,
    tracks : List<Track>?,
    appState : ChillbackAppState,
    index : Int,
    addOnNext : Boolean = false
) : () -> Unit = {
    mediaController?.let { controller ->
        val trackId = track.id.toString()

        // If current track is playing , start from default position
        if (controller.currentMediaItem?.mediaId == trackId) {
            controller.seekToDefaultPosition()
            controller.play()
            return@let
        }

        val mediaItemCount = controller.mediaItemCount

        /// This means this queue is not considered part of the track list
        if (tracks!!.size != mediaItemCount || mediaController.hasNotSameMediaItemsAs(tracks = tracks)) {
            // This means this queue is definitely not part of this track list , hence add it
            // ViewModel scope as it should be a global action
            appState.viewModelScope.launch {
                val mediaItem = track.asMediaItem(appState.musicRepository)

                withContext(Dispatchers.Main) {
                    // Since we add one , hence [int mediaItemIndex] should be [mediaItemCount - 1]
                    if (addOnNext) mediaController.addMediaItem(mediaItem) else mediaController.addMediaItem(
                        controller.currentMediaItemIndex + 1,
                        mediaItem
                    )
                    mediaController.seekToPlay(mediaItemCount)
                }
            }
            return@let
        }

        controller.seekToPlay(index)
    }
}

private fun MediaController.seekToPlay(index : Int) {
    seekToDefaultPosition(index)
    play()
}

private fun List<Track>.sortBasedOn(
    isAscending : Boolean,
    sortMethods : List<Int>,
    details: SnapshotStateList<TrackDetails>
) = sortedWith(
    createCompartorFrom(
        isAscending = isAscending,
        sortMethods = sortMethods,
        details = details
    )
)

private fun createCompartorFrom(
    isAscending : Boolean,
    sortMethods : List<Int>,
    details: SnapshotStateList<TrackDetails>
) : Comparator<Track> {
    val firstValue = sortMethods[0]
    var compartor = if(isAscending) compareBy<Track> {
        compartor(
            track = it,
            details = details[0],
            sortMethod = firstValue
        )
    } else compareByDescending {
        compartor(
            track = it,
            details = details[0],
            sortMethod = firstValue
        )
    }

    sortMethods.subList(1,sortMethods.size).forEachIndexed { index,sortMethod ->
        compartor = compartor.thenBy {
            compartor(
                track = it,
                details = details[index],
                sortMethod = sortMethod
            )
        }
    }

    return compartor
}

private fun compartor(
    track : Track,
    details : TrackDetails,
    sortMethod : Int
) : Comparable<*> = when(sortMethod) {
    0 -> details.name
    1 -> details.genre
    2 -> details.album
    3 -> details.albumArtists ?: ""
    4 -> details.artists ?: ""
    5 -> details.artwork != null
    6 -> track.isFavorite
    else -> throw IllegalStateException("Unreachable condition")
}