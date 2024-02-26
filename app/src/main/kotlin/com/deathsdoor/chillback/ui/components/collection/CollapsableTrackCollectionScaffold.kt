package com.deathsdoor.chillback.ui.components.collection

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import com.deathsdoor.chillback.ui.components.action.BackButton
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
import com.deathsdoor.chillback.ui.components.layout.MultipleRotatedArtworks
import com.deathsdoor.chillback.ui.components.mediaplayer.PlayPauseButton
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState
import com.deathsdoor.chillback.ui.components.track.TrackSongCount
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollapsableTrackCollectionScaffold(
    modifier: Modifier = Modifier,
    repository: TrackCollectionRepository,
    onBack : () -> Unit,
    content : @Composable (trackCollection : TrackCollectionWithTracks?,paddingValues : PaddingValues) -> Unit
) {
    val trackCollection by repository.trackCollection.collectAsState()
    val artworks by collectArtworks(trackCollection = trackCollection)

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    val isExpanded by remember(topAppBarState.collapsedFraction) {
        derivedStateOf { topAppBarState.collapsedFraction < 0.3f }
    }

    val coroutineScope = rememberCoroutineScope()
    val optionState = rememberModalOptionsState(coroutineScope = coroutineScope,skipPartiallyExpanded = true)

    val title = trackCollection.nonNullCollectionName()
    val titleModifier = Modifier.basicMarquee()

    val headerContent : @Composable BoxScope.() -> Unit = {
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp,bottom = 16.dp)) {
            Text(
                modifier = titleModifier,
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )

            TrackSongCount(count = trackCollection?.tracks.orEmpty())

            if(repository.isUserDefined && trackCollection != null) Row {
                TrackSongCount(count = trackCollection!!.tracks)

                Text("•")

                val userRepository = LocalAppState.current.userRepository
                IconButton(
                    onClick = { coroutineScope.launch { userRepository.changeTrackCollectionPinStatus(trackCollection!!.collection) } },
                    content = { TrackCollectionItemPushPinImage(isPinned = trackCollection?.collection?.isPinned)  }
                )
            }
        }
    }

    val playPauseEnterAnimation = fadeIn() + scaleIn()
    val playPauseExitAnimation = fadeOut() + scaleOut()

    val playPauseButton = @Composable { condition : Boolean, innerModifier : Modifier ->

        AnimatedVisibility(
            modifier = innerModifier,
            visible = condition,
            enter = playPauseEnterAnimation,
            exit = playPauseExitAnimation,
            content = { PlayPauseButton()/*(onClick = { /*TODO : Clear list on playing then add it all*/ } )*/ }
        )
    }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { playPauseButton(!isExpanded,Modifier) },
        topBar = {
            MultipleRotatedArtworks(
                modifier = Modifier.applyIf(isExpanded) { fillMaxHeight(0.35f) },
                artworks = artworks,
                content = {
                    LargeTopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        scrollBehavior = scrollBehavior,
                        actions = { MoreInfoButton { optionState.show() } },
                        navigationIcon = { BackButton(onClick = onBack) },
                        title = {
                            if(!isExpanded) Text(modifier = titleModifier, text = title)
                        }
                    )

                    if(isExpanded) headerContent()

                    playPauseButton(isExpanded,Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp,end = 16.dp))
                }
            )

        },
        content = { content(trackCollection,it) }
    )

    trackCollection?.let {
        TrackCollectionExtraOptions(
            state = optionState,
            trackCollection = it,
            repository = repository,
            header = {
                MultipleRotatedArtworks(
                    modifier = Modifier.aspectRatio(1f),
                    artworks = artworks,
                    content = {
                        BackButton(onClick = onBack)
                        headerContent()
                    }
                )
            }
        )
    }
}

@Composable
private fun collectArtworks(trackCollection: TrackCollectionWithTracks?): MutableState<List<Uri>> {
    // Remember as I am using suspend below
    val artworks = remember { mutableStateOf(emptyList<Uri>()) }

    val appState = LocalAppState.current
    LaunchedEffect(Unit) {
        if(trackCollection == null) return@LaunchedEffect
        artworks.value = trackCollection.tracks.mapNotNull { appState.musicRepository.trackArtwork(it) }
    }

    return artworks
}

private fun TrackCollectionWithTracks?.nonNullCollectionName() = this?.collection?.name ?: "Consider Waiting for a second"