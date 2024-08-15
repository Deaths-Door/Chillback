package com.deathsdoor.chillback.core.media.previews

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroMediaMetadata
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.NativeMediaPLayer
import com.deathsdoor.chillback.core.media.LazyMediaItemGrid
import com.deathsdoor.chillback.core.media.components.mediaitem.MediaItemRowItem
import com.deathsdoor.chillback.core.resources.Res
import com.eygraber.uri.Uri
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.rememberReorderableLazyGridState


@Composable
@Preview(showSystemUi = true)
@PreviewScreenSizes
internal fun LazyMediaItemGridPreview() = MaterialTheme {
    val astroPlayer = AstroPlayer(NativeMediaPLayer(ExoPlayer.Builder(LocalContext.current).build()))

    with(object : ReorderableCollectionItemScope {
        override fun Modifier.draggableHandle(
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            onDragStarted: (startedPosition: Offset) -> Unit,
            onDragStopped: () -> Unit,
        ): Modifier = this.then(Modifier)

        override fun Modifier.longPressDraggableHandle(
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            onDragStarted: (startedPosition: Offset) -> Unit,
            onDragStopped: () -> Unit,
        ): Modifier = this.then(Modifier)
    }) {
        MediaItemRowItem(
            astroPlayer = astroPlayer,
            mediaItem = AstroMediaItem(
                mediaId = "1",
                source = Uri.EMPTY,
                metadata = AstroMediaMetadata(
                    displayTitle = "Title",
                    artist = "Artist",
                    genre = "Genre",
                )
            ),
            isSelected = false,
            elevation = 0.dp,
            interactionSource = MutableInteractionSource()
        )
    }



    /*LazyMediaItemGrid(
        astroPlayer =,
        items = List(10) {
            AstroMediaItem(
                mediaId = "1",
                source = Uri.EMPTY,
                metadata = null
            )
        },
        gridState = gridState,
        reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState = gridState) { _, _ -> },
        isSingleItemPerRow = true,
        coroutineScope = rememberCoroutineScope(),
        placeHolderText = Res.strings.app_name
    )*/
}