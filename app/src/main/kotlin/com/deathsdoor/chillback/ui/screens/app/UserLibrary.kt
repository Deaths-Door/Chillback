package com.deathsdoor.chillback.ui.screens.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.playbackqueue.PlaybackQueueRowSection
import com.deathsdoor.chillback.ui.components.statistics.UserStatisticsCard
import com.deathsdoor.chillback.ui.navigation.navigateToFavoritesScreen
import com.deathsdoor.chillback.ui.navigation.navigateToLocalSongsLibraryScreen
import com.deathsdoor.chillback.ui.navigation.navigateToTopPlayedScreen
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.deathsdoor.chillback.ui.state.ChillbackAppState

// Search bar
// Stats || Utility Options
// Playlists
@Composable
fun UserLibrary() = Column {
    val modifier = Modifier.padding(16.dp)
    val windowAdaptiveSize = LocalWindowAdaptiveSize.current
    // TODO : make search bar

    when(windowAdaptiveSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> UtilitySection(modifier = modifier, windowAdaptiveSize =  windowAdaptiveSize)
        else -> Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val weight = Modifier.weight(1f)
            UserStatisticsCard(modifier = weight)
            UtilitySection(modifier = weight,windowAdaptiveSize =  windowAdaptiveSize)
        }
    }
}

private val Blue60 = Color(0xFF2F9EBE)
private val Blue80 = Color(0xFF5DD5FC)

private val Orange60 = Color(0xFFD17A59)
private val Orange80 = Color(0xFFFFB59B)

private val Green60 = Color(0XFF07A859)
private val Green80 = Color(0xFF0EE37C)

private val Pink60 = Color(0xFFEC407A)
private val Pink80 = Color(0xFFFF599C)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UtilitySection(
    modifier: Modifier,
    windowAdaptiveSize: WindowSizeClass
) {
    val appState : ChillbackAppState?
    val mediaController: MediaController?

    if(LocalInspectionMode.current) {
        appState = null
        mediaController = null
    }
    else {
        appState = LocalAppState.current
        mediaController = appState.mediaController
    }

    val isPlaybackQueueShown by remember(mediaController?.mediaItemCount) {
        mutableStateOf(mediaController != null && mediaController.mediaItemCount != 0)
    }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = if(windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Compact) 2 else 1,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        content = {
            val contentModifier = Modifier.weight(1f)

            UtilityOption(
                modifier = contentModifier,
                text = "Favourite",
                painter = rememberVectorPainter(Icons.Default.Favorite),
                background = Blue80,
                tint = Blue60,
                onClick = { appState?.navigateToFavoritesScreen() }
            )

            UtilityOption(
                modifier = contentModifier,
                text = "Top Played",
                painter = painterResource(R.drawable.upward_rising_arrow),
                background = Orange80,
                tint = Orange60,
                onClick = { appState?.navigateToTopPlayedScreen() }
            )

            AnimatedVisibility(isPlaybackQueueShown) {
                UtilityOption(
                    modifier = contentModifier,
                    text = "Queue",
                    painter = painterResource(R.drawable.playback_queue),
                    background = Green80,
                    tint = Green60,
                    onClick = { /*TODO : navigate to QUeue page*/}
                )
            }

            UtilityOption(
                modifier = contentModifier,
                text = "Local Songs",
                // TODO : CHANGE ICON
                painter = painterResource(R.mipmap.application_logo),
                background = Pink80,
                tint = Pink60,
                onClick = { appState?.navigateToLocalSongsLibraryScreen() }
            )
        }
    )

    AnimatedVisibility(isPlaybackQueueShown) {
        PlaybackQueueRowSection(
            modifier = modifier.fillMaxWidth(),
            mediaController = mediaController!!
        )
    }
}

@Composable
@NonRestartableComposable
private fun UtilityOption(
    modifier : Modifier = Modifier,
    text : String,
    painter : Painter,
    background:Color,
    tint : Color,
    onClick : () -> Unit
) = Card(
    colors = CardDefaults.cardColors(background),
    modifier = modifier,
    onClick = onClick,
    content = {
        Thumbnail(
            modifier = Modifier.padding(24.dp),
            title = text,
            artwork = {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = tint,
                )
            }
        )
    }
)
/*
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UtilitySection(modifier: Modifier) {
    val appState : ChillbackAppState?
    val mediaController: MediaController?

    if(LocalInspectionMode.current) {
        appState = null
        mediaController = null
    }
    else {
        appState = LocalAppState.current
        appState.mediaController.also { mediaController = it }
    }

    var isPlaybackQueueShown by remember { mutableStateOf(false) }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        content = {
            val contentModifier = Modifier.weight(1f)

            val appState = LocalAppState.current

            UtilityOption(
                modifier = contentModifier,
                text = "Favourite",
                painter = rememberVectorPainter(Icons.Default.Favorite),
                background = Blue80,
                tint = Blue60,
                onClick = { appState.navigateToFavoritesScreen() }
            )

            UtilityOption(
                modifier = contentModifier,
                text = "Top Played",
                painter = painterResource(R.drawable.upward_rising_arrow),
                background = Orange80,
                tint = Orange60,
                onClick = { appState.navigateToTopPlayedScreen() }
            )

            AnimatedVisibility(mediaController != null && mediaController.mediaItemCount != 0) {
                UtilityOption(
                    modifier = contentModifier,
                    text = "Queue",
                    painter = painterResource(R.drawable.playback_queue),
                    background = Green80,
                    tint = Green60,
                    onClick = { isPlaybackQueueShown = !isPlaybackQueueShown }
                )
            }

            UtilityOption(
                modifier = contentModifier,
                text = "Local Songs",
                // TODO : CHANGE ICON
                painter = painterResource(R.mipmap.application_logo),
                background = Pink80,
                tint = Pink60,
                onClick = { appState.navigateToLocalSongsLibraryScreen() }
            )
        }
    )

    AnimatedVisibility(isPlaybackQueueShown) {
        PlaybackQueueRowSection(
            modifier = modifier.fillMaxWidth(),
            mediaController = mediaController!!
        )
    }
}

*/
/*= Column {
    UserLibrarySearchBar()

    Row(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Playlist",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        // TODO : ADD SORT BUTTON && CHANGE OREINATION BUTTON
        CreateTrackCollectionButton(iconSize = 24.dp)
    }

    val musicRepository = LocalAppState.current.musicRepository
    val collections by musicRepository.userTrackCollections.collectAsState()

    LazyTrackCollectionList(
        modifier = Modifier.padding(top = 16.dp),
        collections = collections,
        onDelete = { musicRepository.deleteUserTrackCollection(it) },
        onPinChange = { musicRepository.changeUserTrackCollectionPinStatus(it) },
        placeHolder = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = "No Playlists created",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserLibrarySearchBar() {
    var active by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    SearchBar(
        active = active,
        onActiveChange = { active = it },
        query = query,
        onQueryChange = { query = it },
        placeholder = {  Text("Search for Media") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = { /* TODO(ADD PROFILE PIC / SETTINGS ICON HERE )*/ },
        onSearch = { TODO() },
        content = {
            // TODO : ADD DIFFERENT SORTING OPTIONS ETC HERE
        }
*/


@Preview
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
internal fun UserLibraryPreview() {
    InitializeProvidersForPreview {
        ChillbackMaterialTheme {
            UserLibrary()
        }
    }
}