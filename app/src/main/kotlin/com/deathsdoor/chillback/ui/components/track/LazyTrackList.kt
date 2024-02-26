package com.deathsdoor.chillback.ui.components.track

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.repositories.MusicRepository
import com.deathsdoor.chillback.ui.components.action.LazyLayoutChangeButton
import com.deathsdoor.chillback.ui.components.layout.LazyDismissibleList
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptions
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO : ADD MULTI SELECT Option to the list
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun LazyTrackList(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope,
    tracks: List<Track>?,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.EndToStart,
        DismissDirection.StartToEnd
    ),
    onRemove : (index : Int,Track) -> Unit,
    onTracksSorted : (sorted : List<Track>) -> Unit
) = Column(modifier = modifier) {
    val isSingleItemPerRow = remember { mutableStateOf(true) }
    val details = remember { mutableStateListOf<TrackDetails>() }

    // TODO : Fix potential issue of sorting list multiple items??
    val optionsEnabled by remember(tracks) { mutableStateOf(!tracks.isNullOrEmpty()) }
    Row(modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
        val optionState= rememberModalOptionsState(coroutineScope = coroutineScope)

        IconButton(
            enabled = optionsEnabled,
            onClick = { optionState.show() },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = null
                )
            }
        )

        if(optionsEnabled) {
            SortFilterSheet(
                state = optionState,
                coroutineScope = coroutineScope,
                tracks = tracks!!,
                details = details,
                onTracksSorted = onTracksSorted
            )
        }

        LazyLayoutChangeButton(isSingleItemPerRow = isSingleItemPerRow,enabled = optionsEnabled)
    }

    LazyDismissibleList(
        items = tracks,
        singleItemPerRow = isSingleItemPerRow.value,
        directions = directions,
        confirmValueChange = { dismissValue, index ,track ->
            if (!isSingleItemPerRow.value && dismissValue == DismissValue.DismissedToStart) {
                onRemove(index,track)
                return@LazyDismissibleList true
            }

            false
        },
        startToEndColor = Color(0xFFCCE5D6),
        endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
        swipeableContent = { isStartToEnd , track ->
            if(isStartToEnd) LikeButton(isLiked = track.isFavorite,enabled = false, onValueChange = {})
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
                        text = styledText(
                            plain0 = "Looks like your ",
                            colored0 = "favorites list is shy!\nExplore",
                            plain1 = " some tunes and mark your jams ",
                            colored1 = "to spice things up"
                        ),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // TODO : Add add tracks to playlist button here , only if user defined == false
                }
            )
        },
        // TODO : ADD PLAY ALL Option
        optionContent = { state, item -> TrackExtraOptions(state,item) },
        content = { index , track , onLongClick ->
            val appState = LocalAppState.current
            val trackDetails = receiveTrackDetails(
                musicRepository = appState.musicRepository,
                details = details,
                track = track,
                index = index,
                run = { LaunchedEffect(Unit) { it() } }
            )

            val mediaController = appState.mediaController
            val userRepository = appState.userRepository

            // TODO : FIX THE GRID LAYOUT LOOKS
            // TODO : FIX THE LIKE BUTTON WEIRD LOOKS
            TrackItem(
                modifier = Modifier.combinedClickable(
                    onClick = {
                        // TODO : CHECK THIS IF IT WORKS
                        mediaController?.let { controller ->
                            val trackId = track.id.toString()

                            // If current track is playing , start from default position
                            if(controller.currentMediaItem?.mediaId == trackId) {
                                controller.seekToDefaultPosition()
                                controller.play()
                                return@combinedClickable
                            }

                            val mediaItemCount = controller.mediaItemCount

                            /// This means this queue is not considered part of the track list
                            var mediaItemIndex = 0
                            if(
                                tracks!!.size != mediaItemCount ||
                                tracks.any {
                                    val isNotSame= controller.getMediaItemAt(index).mediaId != tracks[mediaItemIndex].id.toString()
                                    mediaItemIndex += 1
                                    isNotSame
                                }
                            ) {
                                // This means this queue is definitely not part of this track list , hence add it
                                // ViewModel scope as it should be a global action
                                appState.viewModelScope.launch {
                                    val mediaItem = track.asMediaItem(appState.musicRepository)

                                    withContext(Dispatchers.Main) {
                                        // Since we add one , hence [int mediaItemIndex] should be [mediaItemCount - 1]
                                        mediaController.addMediaItem(mediaItem)
                                        mediaController.seekToPlay(mediaItemCount)
                                    }
                                }
                                return@combinedClickable
                            }

                            controller.seekToPlay(index)
                        }
                    },
                    onClickLabel = "Play this track",
                    onLongClick = onLongClick,
                    onLongClickLabel = "Show extra options for track",
                ),
                track = track,
                isSingleItemPerRow = isSingleItemPerRow.value,
                onLikeChange = { userRepository.changeFavouriteStatusForTrack(track = track,status = it) },
                details = trackDetails
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
    if(detail == null) run { details.add(index,musicRepository.trackDetails(track)) }
    return detail
}

private fun MediaController.seekToPlay(index : Int) {
    seekToDefaultPosition(index)
    play()
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SortFilterSheet(
    state : ModalOptionsState,
    coroutineScope : CoroutineScope,
    details : SnapshotStateList<TrackDetails>,
    tracks: List<Track>,
    onTracksSorted : (sorted : List<Track>) -> Unit
) = ModalOptions(
    state = state,
    content = {
        val modifier = Modifier.padding(horizontal = 12.dp)
        val maxWidth = modifier.fillMaxWidth()

        Text(
            modifier = maxWidth,
            text = "Sort and Filter",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = modifier,
            text = "Order",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        var isAscendingOrder by remember { mutableStateOf(true) }
        val sortMethods = remember { mutableStateListOf<Int>() }

        val arrangement = Arrangement.spacedBy(8.dp)

        Row(modifier = modifier,horizontalArrangement = arrangement) {
            FilterChip(
                selected = isAscendingOrder,
                onClick = { isAscendingOrder = true },
                label = { Text("Ascending") },
                leadingIcon = ifSelectedLeadingIcon(selected = isAscendingOrder,label = "Ascending")
            )

            FilterChip(
                selected = !isAscendingOrder,
                onClick = { isAscendingOrder = false },
                label = { Text("Descending") },
                leadingIcon = ifSelectedLeadingIcon(selected = !isAscendingOrder,label = "Descending")
            )
        }

        Divider(modifier = maxWidth.padding(horizontal = 8.dp))

        Text(
            modifier = modifier,
            text = "Sort By Multiple",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium

        )

        FlowRow(modifier = modifier,horizontalArrangement = arrangement, verticalArrangement = arrangement) {
            sortItems.forEachIndexed { index, label ->
                val selected = sortMethods.contains(index)
                FilterChip(
                    selected = selected,
                    onClick = { if(selected) sortMethods.removeAt(index) else sortMethods.add(index) },
                    label = { Text(label) },
                    leadingIcon = ifSelectedLeadingIcon(selected = selected,label = label)
                )
            }
        }

        var isDialogOpen by remember { mutableStateOf(false) }

        val sortTracks = {
            onTracksSorted(
                tracks.sortBasedOn(
                    isAscending = isAscendingOrder,
                    sortMethods = sortMethods,
                    details = details
                )
            )
        }

        Row(modifier = maxWidth,horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(
                enabled = sortMethods.isNotEmpty() || !isAscendingOrder,
                onClick = { isAscendingOrder = true;sortMethods.clear() },
                content = {
                    Text("Reset All")
                    Badge(modifier = Modifier.align(Alignment.CenterVertically)) { Text("${sortMethods.size}") }
                }
            )

            Button(
                onClick = {
                    if(tracks.size != details.size) isDialogOpen = true
                    else coroutineScope.launch {
                        sortTracks()
                    }
                },
                content = { Text("Apply") }
            )
        }

        if(!isDialogOpen) return@ModalOptions

        var waitingForDetails by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Fetch Details and then sort?") },
            text = {
                if(waitingForDetails) {
                    Text(text = "Fetching details ...", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.padding(16.dp).fillMaxWidth())
                    return@AlertDialog
                }

                Text("Some details are missing. Sort with available info now, or wait for all to load?")
            },
            dismissButton = {
                Button(
                    onClick = {
                        coroutineScope.launch { sortTracks() }
                        isDialogOpen = false
                        state.dismiss()
                    },
                    content = { Text(text = "No") }
                )
            },
            confirmButton = {
                val musicRepository = LocalAppState.current.musicRepository
                TextButton(
                    enabled = !waitingForDetails,
                    content = {
                        Text(
                            text = "Wait",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        waitingForDetails = true

                        coroutineScope.launch {
                            details.subList(0,tracks.size).forEachIndexed { index, _ ->
                                receiveTrackDetails(
                                    musicRepository = musicRepository,
                                    details = details,
                                    track = tracks[index],
                                    index = index,
                                    run = { it() }
                                )
                            }

                            launch { sortTracks() }

                            isDialogOpen = false
                            state.dismiss()
                        }
                    }
                )
            }
        )
    }
)

private val sortItems by lazy { listOf("Name","Genre","Album","Album Artists","Artists","Has Artwork","Is Favourite") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ifSelectedLeadingIcon(selected : Boolean,label : String): (@Composable () -> Unit)? = if(selected) {{
    Icon(
        imageVector = Icons.Filled.Done,
        contentDescription = "Selected $label",
        modifier = Modifier.size(FilterChipDefaults.IconSize)
    )
}} else null

private fun List<Track>.sortBasedOn(
    isAscending : Boolean,
    sortMethods : SnapshotStateList<Int>,
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
    sortMethods : SnapshotStateList<Int>,
    details: SnapshotStateList<TrackDetails>
) : Comparator<Track> {
    val firstValue = sortMethods.removeFirst()
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

    sortMethods.forEachIndexed { index,sortMethod ->
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