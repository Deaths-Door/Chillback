package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.repositories.FavouriteTrackCollectionRepository
import com.deathsdoor.chillback.ui.components.track.LazyTrackList
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState

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
                onRemove = {  track ->
                    if(repository.isUserDefined) return@LazyTrackList
                    userRepository.removeTrackFromCollection(trackCollection!!.collection,track,trackCollection.tracks.indexOf(track))
                },
                onTracksSorted = {
                    if(repository.isUserDefined) return@LazyTrackList
                    userRepository.rearrangeTracks(trackCollection!!.collection,trackCollection.tracks)
                },
                placeHolderText = when (repository) {
                    is FavouriteTrackCollectionRepository -> {{
                        styledText(
                            plain0 = "Looks like your ",
                            colored0 = "favorites list is shy!\nExplore",
                            plain1 = " some tunes and mark your jams ",
                            colored1 = "to spice things up"
                        )
                    }}
                    // Default Value
                    else -> {{
                        styledText(
                            plain0 = "There are no track to display yet.\n",
                            colored0 = "Explore",
                            plain1 = " and discover some music ",
                            colored1 = "to fill your library!"
                        )
                    }}
                }
            )
        }
    )
}