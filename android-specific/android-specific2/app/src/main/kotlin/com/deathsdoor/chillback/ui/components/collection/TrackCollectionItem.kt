package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.repositories.UserRepository
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.components.action.MoreInfoButton
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnail
import com.deathsdoor.chillback.ui.components.layout.SelectableThumbnailCard
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.components.layout.applyToggleableOnSelection
import com.deathsdoor.chillback.ui.components.track.ArtworkWithFailureInformer
import com.deathsdoor.chillback.ui.components.track.TrackSongCount
import com.deathsdoor.chillback.ui.extensions.themeBasedTint
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.core.rememberDragSelectState
import kotlinx.coroutines.delay

@Composable
@NonRestartableComposable
fun TrackCollectionCard(
    modifier : Modifier = Modifier,
    collection: TrackCollection,
    isSelected : Boolean?,
    draggableState : DragSelectState<TrackCollection>,
    isPinnable  : Boolean = false
) {
    val userRepository = if(LocalInspectionMode.current) null else LocalAppState.current.userRepository

    val textModifier = Modifier.padding(start = 16.dp)

    SelectableThumbnailCard(
        modifier = modifier.applyToggleableOnSelection(
            item = collection,
            isSelected = isSelected,
            draggableState = draggableState
        ),
        isSelected = isSelected,
        title = {
            ThumbnailTitle(
                modifier = textModifier,
                text = collection.name,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        artwork = {
            ArtworkWithFailureInformer(
                modifier = Modifier
                    .padding(12.dp)
                    .matchParentSize(),
                model = collection.imageSource,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        },
        caption  = {
            AnimatedTrackSongCount(
                modifier = textModifier,
                collection = collection,
                userRepository = userRepository
            )
        },
        actionIcon = if(isPinnable) {{ actionModifier , enabled ->
            IconToggleButton(
                modifier = actionModifier,
                enabled = enabled,
                checked = collection.isPinned,
                onCheckedChange = { userRepository?.changeTrackCollectionPinStatus(collection) },
                content = { TrackCollectionItemPushPinImage(isPinned = collection.isPinned) }
            )

            MoreInfoButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
                content = {
                    // TODO : Show more info for trackCollection
                }
            )
        }} else null
    )
}

@Composable
@NonRestartableComposable
fun TrackCollectionRowItem(
    modifier : Modifier = Modifier,
    collection: TrackCollection,
    isSelected : Boolean?,
    draggableState : DragSelectState<TrackCollection>,
    isPinnable  : Boolean = false
) {
    val userRepository = if(LocalInspectionMode.current) null else LocalAppState.current.userRepository

    SelectableThumbnail(
        modifier = modifier
            .fillMaxWidth()
            .applyToggleableOnSelection(
                item = collection,
                isSelected = isSelected,
                draggableState = draggableState
            ),
        isSelected = isSelected,
        title = {
            ThumbnailTitle(
                text = collection.name,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        artwork = {
            ArtworkWithFailureInformer(
                modifier = Modifier.size(64.dp),
                model = collection.imageSource,
                contentDescription = null,
            )
        },
        caption  = {
            AnimatedTrackSongCount(
                collection = collection,
                userRepository = userRepository
            )
        },
        actionIcon = if(isPinnable) {{ actionModifier , enabled ->
            IconToggleButton(
                modifier = actionModifier,
                enabled = enabled,
                checked = collection.isPinned,
                onCheckedChange = { userRepository?.changeTrackCollectionPinStatus(collection) },
                content = { TrackCollectionItemPushPinImage(isPinned = collection.isPinned) }
            )
        }} else null,
        trailingIcon = {
            MoreInfoButton {
                // TODO : Show more info for trackCollection
            }
        }
    )
}

@Composable
@NonRestartableComposable
fun TrackCollectionItemPushPinImage(modifier : Modifier = Modifier,isPinned : Boolean?) = Icon(
    modifier = modifier.rotate(45f),
    painter = painterResource(id = R.drawable.push_pin),
    contentDescription = if(isPinned == true) "Pin" else "Unpin",
    tint = themeBasedTint()
)

@Composable
private fun AnimatedTrackSongCount(
    modifier: Modifier = Modifier,
    collection: TrackCollection,
    userRepository: UserRepository?,
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(collection.isPinned) {
        isVisible = false
        delay(250)
        isVisible = true
    }

    var count by remember { mutableIntStateOf(0) }

    userRepository?.let {
        LaunchedEffect(Unit) {
            count = it.tracksFor(collection)
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { w -> w }) + expandHorizontally(expandFrom = Alignment.End),
        exit = slideOutHorizontally(targetOffsetX = { w -> w }) + shrinkHorizontally(shrinkTowards = Alignment.End),
        content = {
            TrackSongCount(
                modifier = Modifier.padding(end = 16.dp),
                count = count,
                style = MaterialTheme.typography.labelMedium
            )
        }
    )
}

@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
@Composable
@NonRestartableComposable
fun TrackCollectionCardPreview() = InitializeProvidersForPreview {
    ChillbackMaterialTheme {
        Surface {
           TrackCollectionCard(
               collection = TrackCollection("test"),
               isSelected = false,
               draggableState = rememberDragSelectState()
           )
        }
    }
}