package com.deathsdoor.chillback.core.media.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import dev.icerock.moko.resources.compose.painterResource
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
fun TrackArtwork(
    mediaItem: AstroMediaItem,
    modifier : Modifier = Modifier,
    contentScale : ContentScale = ContentScale.Fit
) {
    val snackBarState= LocalSnackbarState.current
    val placeHolderImage = painterResource(Res.images.track_placeholder)

    LazyResource {
        AsyncImage(
            modifier = modifier,
            model = mediaItem.source,
            contentDescription = null,
            contentScale = contentScale,
            placeholder = placeHolderImage,
            error = placeHolderImage,
            fallback = placeHolderImage,
            onError = {
                snackBarState.showWarningSnackbar(
                    title = stringResource(Res.strings.failed_loading_artwork)
                )
            }
        )
    }
}