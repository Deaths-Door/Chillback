package com.deathsdoor.chillback.core.media

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.media.components.LazySelectableSwipeableDraggableGrid
import com.deathsdoor.chillback.core.media.components.LikeButton
import com.deathsdoor.chillback.core.media.components.MediaItemExtraOptions
import com.deathsdoor.chillback.core.media.state.LazySelectableState
import com.deathsdoor.chillback.core.media.state.rememberLazySelectableState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
fun LazyMediaItemGrid(
    modifier : Modifier = Modifier,
    selectableState: LazySelectableState<AstroMediaItem> = rememberLazySelectableState(),
    astroPlayer: AstroPlayer,
    items : List<AstroMediaItem>,
    isSingleItemPerRow : Boolean,
    coroutineScope: CoroutineScope,
) = LazySelectableSwipeableDraggableGrid(
    modifier = modifier,
    selectableState = selectableState,
    items = items,
    key = { it.mediaId },
    isSingleItemPerRow = isSingleItemPerRow,
    coroutineScope = coroutineScope,
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
    optionContent = { index, item, onDismiss ->
        MediaItemExtraOptions(
            mediaItem = item,
            onDismiss = onDismiss
        )
    },

)