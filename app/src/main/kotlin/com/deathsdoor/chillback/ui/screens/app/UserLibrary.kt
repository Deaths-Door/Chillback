package com.deathsdoor.chillback.ui.screens.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.components.collection.LazyTrackCollectionList
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.playbackqueue.PlaybackQueueRowSection
import com.deathsdoor.chillback.ui.navigation.navigateToFavoritesScreen
import com.deathsdoor.chillback.ui.navigation.navigateToLocalSongsLibraryScreen
import com.deathsdoor.chillback.ui.navigation.navigateToTopPlayedScreen
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.deathsdoor.chillback.ui.state.ChillbackAppState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserLibrary() {
    val windowAdaptiveSize = LocalWindowAdaptiveSize.current
    val width = windowAdaptiveSize.widthSizeClass
    val height = windowAdaptiveSize.heightSizeClass

    val modifier = Modifier.padding(16.dp)

    // TODO : Search bar + USER library + Create new playlist button(for all screens except desktop where , it could be on the lazyoptionsrow composable row) text here
    when {
        width == WindowWidthSizeClass.Expanded -> when(height) {
            // Landscape Phone
            WindowHeightSizeClass.Compact -> Row {
                val style = MaterialTheme.typography.labelSmall

                UtilitySection(
                    modifier = modifier.weight(1f),
                    maxItemsInEachRow = 1,
                    style = style,
                    utility = {
                        StatisticUtilityOption(modifier = it,style = style)
                    }
                )

                UserPlaylists(modifier = Modifier.weight(1f))
            }
            // Desktop / Large screens
            else -> Column {
                UtilitySection(
                    modifier = modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    utility = {
                        StatisticUtilityOption(modifier = it)

                        UtilityOption(
                            modifier = it,
                            text = "Playback Queue",
                            painter = painterResource(R.drawable.playback_queue),
                            background = Red80,
                            tint = Red60,
                            onClick  = {

                            }
                        )
                    }
                )

                UserPlaylists()
            }
        }
        // Portrait Phone / unfolded device
        else -> Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    Text(
                        text = "Statistics",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    IconButton(
                        onClick = { TODO("NAVIGATE TO STATISCS SCREEN") },
                        content = {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Navigate to view your Statistics",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    )
                }
            )

            UtilitySection(
                modifier = modifier,
                maxItemsInEachRow = 2,
                content = { mediaController , isPlaybackQueueShown ->
                    AnimatedVisibility(isPlaybackQueueShown) {
                        PlaybackQueueRowSection(
                            modifier = modifier.fillMaxWidth(),
                            mediaController = mediaController!!
                        )
                    }
                }
            )

            UserPlaylists()
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

private val Red60 =  Color(0xFFC02942)
private val Red80 = Color(0xFFE3425B)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UtilitySection(
    modifier: Modifier,
    maxItemsInEachRow : Int,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    utility : (@Composable FlowRowScope.(Modifier) -> Unit)? = null,
    content : (@Composable (Player?,Boolean) -> Unit)? = null
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
        maxItemsInEachRow = maxItemsInEachRow,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        content = {
            val contentModifier = Modifier.weight(1f)

            UtilityOption(
                modifier = contentModifier,
                text = "Favourite",
                style = style,
                painter = rememberVectorPainter(Icons.Default.Favorite),
                background = Blue80,
                tint = Blue60,
                onClick = { appState?.navigateToFavoritesScreen() }
            )

            UtilityOption(
                modifier = contentModifier,
                text = "Top Played",
                style =  style,
                painter = painterResource(R.drawable.upward_rising_arrow),
                background = Orange80,
                tint = Orange60,
                onClick = { appState?.navigateToTopPlayedScreen() }
            )

            AnimatedVisibility(isPlaybackQueueShown) {
                UtilityOption(
                    modifier = contentModifier,
                    text = "Queue",
                    style =  style,
                    painter = painterResource(R.drawable.playback_queue),
                    background = Green80,
                    tint = Green60,
                    onClick = { /*TODO : navigate to QUeue page*/}
                )
            }

            UtilityOption(
                modifier = contentModifier,
                text = "Local Songs",
                style = style,
                // TODO : CHANGE ICON
                painter = painterResource(R.drawable.lib),
                background = Pink80,
                tint = Pink60,
                onClick = { appState?.navigateToLocalSongsLibraryScreen() }
            )

            utility?.let { it(contentModifier) }
        }
    )

    content?.let { it(mediaController,isPlaybackQueueShown) }
}

@Composable
@NonRestartableComposable
private fun StatisticUtilityOption(
    modifier : Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) = UtilityOption(
    modifier = modifier,
    style = style,
    text = "Statistics",
    painter = painterResource(R.drawable.analytics),
    background = Green80,
    tint = Green60,
    onClick = {
        // TODO
    }
)

@Composable
@NonRestartableComposable
private fun UtilityOption(
    modifier : Modifier = Modifier,
    text : String,
    style : TextStyle = MaterialTheme.typography.bodyMedium,
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
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp),
            title = text,
            textStyle = style,
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


@Composable
private fun UserPlaylists(modifier: Modifier = Modifier) {
    val userRepository = LocalAppState.current.userRepository

    val coroutineScope = rememberCoroutineScope()
    val collections by userRepository.userTrackCollections.collectAsState()

    LazyTrackCollectionList(
        modifier = modifier.padding(top = 16.dp),
        coroutineScope = coroutineScope,
        collections = collections,
        onDelete = { userRepository.deleteTrackCollection(it) },
        onPinChange = { userRepository.changeTrackCollectionPinStatus(it) },
        placeHolderText = {
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("No Playlists created")
                }
            }
        },
        placeHolderContent = {
            // TODO : Implement this
            // CreateTrackCollectionButton(iconSize = 24.dp)
        }
    )
}

/*=
    Row(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Playlist",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
*/


@PreviewScreenSizes
@Composable
internal fun UserLibraryPreview() {
    InitializeProvidersForPreview {
        ChillbackMaterialTheme {
            UserLibrary()
        }
    }
}