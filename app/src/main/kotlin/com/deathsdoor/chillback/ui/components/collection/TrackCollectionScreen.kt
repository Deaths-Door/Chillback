package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.ui.components.track.LazyTrackList
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCollectionScreen(repository: TrackCollectionRepository) {
    val appState = LocalAppState.current
    val userRepository = appState.userRepository
    val navController = appState.navController

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        repository.applyCoroutineScope(coroutineScope)
    }

    CollapsableTrackCollectionScaffold(
        repository = repository,
        onBack = { navController.popBackStack() },
        content = { trackCollection, paddingValues ->
            LazyTrackList(
                coroutineScope = coroutineScope,
                modifier = Modifier.padding(paddingValues),
                tracks = trackCollection?.tracks,
                onRemove = { index , track ->
                    if(repository.isUserDefined) return@LazyTrackList
                    userRepository.removeTrackFromCollection(trackCollection!!.collection,track,index)
                },
                onTracksSorted = {
                    if(repository.isUserDefined) return@LazyTrackList
                    userRepository.rearrangeTracks(trackCollection!!.collection,it)
                }
            )
        }
    )
}