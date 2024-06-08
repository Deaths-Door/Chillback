package com.deathsdoor.chillback.core.media

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.extensions.applyOnNull
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.core.media.components.LazySelectableSwipeableDraggableGrid
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.core.media.components.mediaitem.MediaItemCard
import com.deathsdoor.chillback.core.media.components.mediaitem.MediaItemExtraOptions
import com.deathsdoor.chillback.core.media.components.mediaitem.MediaItemRowItem
import com.deathsdoor.chillback.core.media.extensions.onMediaItemClick
import com.deathsdoor.chillback.core.media.repositories.MusixRepository
import com.deathsdoor.chillback.core.media.state.LazySelectableState
import com.deathsdoor.chillback.core.media.state.rememberLazySelectableState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import com.deathsdoor.chillback.core.media.resources.Res
import com.deathsdoor.chillback.core.media.state.rememberIsSelected
import com.deathsdoor.chillback.core.media.state.selectSemantics
import com.deathsdoor.chillback.core.media.state.selectToggleable
import dev.icerock.moko.resources.StringResource
import sh.calvin.reorderable.ReorderableLazyGridState

// TODO; add header as a new function
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyMediaItemGrid(
    modifier : Modifier = Modifier,
    selectableState: LazySelectableState<AstroMediaItem> = rememberLazySelectableState(),
    astroPlayer: AstroPlayer,
    items : List<AstroMediaItem>,
    gridState: LazyGridState,
    @Suppress("SpellCheckingInspection")
    reorderableLazyGridState : ReorderableLazyGridState,
    isSingleItemPerRow : Boolean,
    coroutineScope: CoroutineScope,
    placeHolderText : StringResource,
    onRemove : ((AstroMediaItem) -> Unit)? = null,
    placeHolderContent: (@Composable ColumnScope.() -> Unit)? = null
) = LazySelectableSwipeableDraggableGrid(
    modifier = modifier,
    items = items,
    key = { it.mediaId },
    isSingleItemPerRow = isSingleItemPerRow,
    gridState = gridState,
    reorderableLazyGridState = reorderableLazyGridState,
    coroutineScope = coroutineScope,
    confirmValueChange = { item ->
        if (this == SwipeToDismissBoxValue.EndToStart && onRemove != null) {
            onRemove(item)
            return@LazySelectableSwipeableDraggableGrid true
        }

        false
    },

    startToEndColor = Color(0xFFCCE5D6),
    endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
    swipeableContent = { isStartToEnd, item ->
        when(isStartToEnd) {
            true -> LikeButton(
                mediaItem = item,
                astroPlayer = astroPlayer,
            )
            false -> Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(Res.strings.delete),
                tint = MaterialTheme.colorScheme.error,
            )
        }
    },

    optionContent = { _, item, onDismiss ->
        MediaItemExtraOptions(
            astroPlayer = astroPlayer,
            coroutineScope = coroutineScope,
            mediaItem = item,
            mediaItems = items,
            onRemove = onRemove,
            onDismiss = onDismiss
        )
    },
    placeHolder = {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                Text(
                    text = stringResource(placeHolderText),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                placeHolderContent?.invoke(this)
            }
        )
    },
    content = { item, interactionSource, elevation, onLongClick ->
        CardDefaults.cardElevation()
        val snackBarState = LocalSnackbarState.current
        val isSelected by selectableState.rememberIsSelected(item)

        LazyResource {
            var canShownExtraOptions by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                // TODO; add method to fetch non local music metadata
                val metadata = MusixRepository.metadataFromCacheOrFetch(
                    resource = this@LazyResource,
                    mediaItem = item,
                    stackableSnackbarState = snackBarState
                )

                canShownExtraOptions = metadata != null
            }

            val contentModifier = Modifier.selectSemantics(
                selectableState = selectableState,
                item = item,
                label = { this@LazyResource.stringResource(Res.strings.toggle_select) }
            ).selectToggleable(
                selectableState = selectableState,
                item = item,
                selected = isSelected,
                interactionSource = interactionSource
            ).applyOnNull(isSelected) {
                combinedClickable(
                    onClick = {
                        onMediaItemClick(
                            astroPlayer = astroPlayer,
                            coroutineScope = coroutineScope,
                            mediaItem = item,
                            mediaItems = items,
                        )
                    },
                    onClickLabel = stringResource(Res.strings.play_track),
                    onLongClick = if(canShownExtraOptions) onLongClick else null,
                    onLongClickLabel = if(canShownExtraOptions) stringResource(Res.strings.show_track_extra_options) else null,
                )
            }

            when(isSingleItemPerRow) {
                true -> MediaItemRowItem(
                    modifier = contentModifier,
                    astroPlayer = astroPlayer,
                    mediaItem = item,
                    isSelected = isSelected,
                    elevation = elevation,
                    interactionSource = interactionSource
                )
                false -> MediaItemCard(
                    modifier = contentModifier,
                    astroPlayer = astroPlayer,
                    mediaItem = item,
                    isSelected = isSelected,
                    elevation = elevation,
                    interactionSource = interactionSource
                )
            }
        }
    }
)