package com.deathsdoor.chillback.ui.screens.playbackqueue

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.extensions.mapMediaItems
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.action.BackButton
import com.deathsdoor.chillback.ui.components.track.LazyTrackList
import com.deathsdoor.chillback.ui.providers.LocalAppState

// TODO : Save as playlist
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackQueueScreen() {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberLazyGridState()
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Now Playing") },
                navigationIcon = {
                    val navController = LocalAppState.current.navController
                    BackButton { navController.popBackStack() }
                },
            )
        },
        floatingActionButton = {
            val isScrollingUp by state.isScrollingUp()
            ExtendedFloatingActionButton(
                expanded = isScrollingUp,
                text = { Text("Jump to Active Track") },
                // TODO: Improve Icons , maybe use jump icon
                icon = { Icon(Icons.Default.KeyboardArrowDown,null) },
                onClick = { /*TODO*/ })
        },
        content = { paddingValues ->
            // TODO : Make it rearrangable
            LazyTrackList(
                modifier = Modifier.padding(paddingValues),
                coroutineScope = coroutineScope,
                tracks = LocalAppState.current.mediaController?.mapMediaItems { Track.from(it) },
                onTracksSorted = {
                    // TODO:Rearrange tem
                },
                placeHolderText = {
                    TODO("say no tracks playing , queue is empty")
                }
            )
        }
    )
}

@Composable
private fun LazyGridState.isScrollingUp() : State<Boolean> {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }

    return remember(this)  {
        derivedStateOf {
            val isScrollingUp = if(previousIndex != firstVisibleItemIndex) previousIndex > firstVisibleItemIndex
            else previousIndex >= firstVisibleItemIndex

            previousIndex = firstVisibleItemIndex
            previousScrollOffset = firstVisibleItemScrollOffset

            isScrollingUp
        }
    }
}