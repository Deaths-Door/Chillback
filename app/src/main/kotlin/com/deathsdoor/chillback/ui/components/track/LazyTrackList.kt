package com.deathsdoor.chillback.ui.components.track

import StackedSnakbarHostState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.media3.common.Player
import com.deathsdoor.chillback.data.extensions.hasNotSameMediaItemsAs
import com.deathsdoor.chillback.data.extensions.orJustReport
import com.deathsdoor.chillback.data.extensions.orReport
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.repositories.MusicRepository
import com.deathsdoor.chillback.ui.components.action.LazyOptionsRow
import com.deathsdoor.chillback.ui.components.action.createCompartorFrom
import com.deathsdoor.chillback.ui.components.action.rememberIsSingleItemRow
import com.deathsdoor.chillback.ui.components.layout.LazyDismissibleSelectableList
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.deathsdoor.chillback.ui.state.ChillbackAppState
import com.dragselectcompose.core.rememberDragSelectState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyTrackList(
    modifier : Modifier = Modifier,
    mediaController: Player? = LocalAppState.current.mediaController,
    coroutineScope: CoroutineScope,
    tracks: List<Track>?,
    onRemove : ((Track) -> Unit)? = null,
    onTracksSorted : (() -> Unit)? = null,
    placeHolderText : @Composable () -> AnnotatedString = {
        styledText(
            plain0 = "There are no track to display yet.\n",
            colored0 = "Explore",
            plain1 = " and discover some music ",
            colored1 = "to fill your library!"
        )
    },
    placeHolderContent: (@Composable ColumnScope.() -> Unit)? = null
) = Column(modifier = modifier) {

    val isSingleItemPerRow = rememberIsSingleItemRow()
    val draggableState = rememberDragSelectState<Track>()

    val details = remember { mutableStateListOf<TrackDetails>() }
    val appState = LocalAppState.current
    val stackedSnackbarHostState = LocalSnackbarState.current

    LazyOptionsRow(
        coroutineScope = coroutineScope,
        isSingleItemPerRow = isSingleItemPerRow,
        draggableState = draggableState,
        data = tracks,
        criteria = listOf("Name","Genre","Album","Album Artists","Artists","Has Artwork","Is Favourite"),
        fetch = { tracks!!.size != details.size },
        onFetch = {
            details.subList(0,tracks!!.size).forEachIndexed { index, _ ->
                receiveTrackDetails(
                    stackedSnackbarHostState = stackedSnackbarHostState,
                    musicRepository = appState.musicRepository,
                    details = details,
                    index = index,
                    track = tracks[index],
                    tracks = tracks,
                    onRemove = onRemove,
                    run = { it() }
                )
            }
        },
        onSort = { isAscendingOrder , appliedMethods ->
            tracks!!.sortBasedOn(
                isAscending = isAscendingOrder,
                sortMethods = appliedMethods,
                details = details
            )

            onTracksSorted?.invoke()
        }
    )

    LazyDismissibleSelectableList(
        coroutineScope = coroutineScope,
        items = tracks,
        key = { it.id },
        isSingleItemPerRow = isSingleItemPerRow.value,
        confirmValueChange = { dismissValue , item ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart && onRemove != null) {
                onRemove(item)
                return@LazyDismissibleSelectableList true
            }

            false
        },
        startToEndColor = Color(0xFFCCE5D6),
        endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
        swipeableContent = { isStartToEnd, item ->
            if(isStartToEnd) LikeButton(
                track = item,
                mediaController = mediaController,
                enabled = false
            )
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
                        text = placeHolderText(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    placeHolderContent?.invoke(this)
                }
            )
        },
        optionContent = { index , item, onDismiss ->
            TrackExtraOptions(
                track = item,
                details = details[index],
                tracks = tracks,
                onDismissRequest = onDismiss,
                onRemove = onRemove
            )
        },
        content = { contentModifier, item, isSelected, onLongClick ->
            val trackDetails = receiveTrackDetails(
                stackedSnackbarHostState = stackedSnackbarHostState,
                musicRepository = appState.musicRepository,
                details = details,
                track = item,
                tracks = tracks!!,
                onRemove = onRemove,
                run = { LaunchedEffect(Unit) { it() } }
            )

            @Suppress("NAME_SHADOWING")
            val contentModifier = contentModifier.applyIf(isSelected == null) {
                combinedClickable(
                    onClick = {
                        onTrackItemClick(
                            mediaController = mediaController,
                            track = item,
                            tracks = tracks,
                            appState = appState,
                            stackedSnackbarHostState = stackedSnackbarHostState
                        )
                    },
                    onClickLabel = "Play this track",
                    onLongClick = if(trackDetails != null) onLongClick else null,
                    onLongClickLabel = if(trackDetails != null) "Show extra options for track" else null,
                )
            }


            if(isSingleItemPerRow.value) TrackRowItem(
                modifier = contentModifier,
                mediaController = mediaController,
                track = item,
                details = trackDetails,
                isSelected = isSelected,
                draggableState = draggableState,
                tracks = tracks,
                onRemove = onRemove,

            ) else TrackCard(
                modifier = contentModifier,
                mediaController = mediaController,
                track = item,
                details = trackDetails,
                isSelected = isSelected,
                draggableState = draggableState,
                tracks = tracks,
                onRemove = onRemove
            )
        }
    )
}


fun onTrackItemClick(
    stackedSnackbarHostState: StackedSnakbarHostState,
    mediaController: Player?,
    track : Track,
    tracks : List<Track>?,
    appState : ChillbackAppState,
    addOnNext : Boolean = false
) {
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
                track.asMediaItem(appState.musicRepository).orReport(stackedSnackbarHostState,"Cannot play mediaItem as we failed to read its data") {
                    withContext(Dispatchers.Main) {
                        // Since we add one , hence [int mediaItemIndex] should be [mediaItemCount - 1]
                        if (addOnNext) mediaController.addMediaItem(it) else mediaController.addMediaItem(
                            controller.currentMediaItemIndex + 1,
                            it
                        )
                        mediaController.seekToPlay(mediaItemCount)
                    }
                }
            }
            return@let
        }

        controller.seekToPlay(tracks.indexOf(track))
    }
}

private fun Player.seekToPlay(index : Int) {
    seekToDefaultPosition(index)
    play()
}

private inline fun receiveTrackDetails(
    details : SnapshotStateList<TrackDetails>,
    musicRepository: MusicRepository,
    stackedSnackbarHostState : StackedSnakbarHostState,
    track : Track,
    index : Int? = null,
    tracks: List<Track>,
    noinline onRemove : ((Track) -> Unit)? = null,
    run: (receiver : suspend () -> Unit) -> Unit
): TrackDetails? {
    @Suppress("NAME_SHADOWING")
    val index = index ?: tracks.indexOf(track)
    val detail = details.getOrNull(index)
    if(detail == null) run {
        val value = musicRepository.trackDetails(track).orJustReport(stackedSnackbarHostState,description = "Hence removing track from list")
        if(value == null) onRemove?.invoke(track)
        else details.add(index,value)
    }
    return detail
}

private fun List<Track>.sortBasedOn(
    isAscending : Boolean,
    sortMethods : List<Int>,
    details: SnapshotStateList<TrackDetails>
): List<Track> {
    var index = 0
    return sortedWith(
        createCompartorFrom(
            isAscending = isAscending,
            sortMethods = sortMethods,
            create = { track , method ->
                val detail = details[index]
                index += 1
                when(method) {
                    0 -> detail.name
                    1 -> detail.genre
                    2 -> detail.album
                    3 -> detail.albumArtists ?: ""
                    4 -> detail.artists ?: ""
                    5 -> detail.artwork != null
                    6 -> track.isFavorite
                    else -> throw IllegalStateException("Unreachable condition")
                }
            }
        )
    )
}