package com.deathsdoor.chillback.ui.components.track

import StackedSnackbarDuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState

@Composable
@NonRestartableComposable
@Deprecated("Do not use this")
fun Artwork(
    modifier : Modifier = Modifier,
    uri : Uri?,
    contentScale: ContentScale = ContentScale.Fit,
) = Image(
    modifier = modifier.clip(RoundedCornerShape(8.dp)),
    painter = uri?.let { rememberAsyncImagePainter(uri,onState = { Log.d("image","$it -> ${if(it is AsyncImagePainter.State.Error) it.result.throwable else ""} ") }) } ?: painterResource(id =R.mipmap.application_logo),
    contentDescription = null,
    contentScale = contentScale,
)

@Composable
@NonRestartableComposable
fun ArtworkWithFailureInformer(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeHolder: Painter = painterResource(id = R.mipmap.application_logo),
    contentScale: ContentScale = ContentScale.Fit
) {
    val snackbar = LocalSnackbarState.current

    AsyncImage(
        modifier = modifier,
        model = model,
        contentScale = contentScale,
        contentDescription = contentDescription,
        onError = {
            snackbar.showErrorSnackbar(
                title = "Picture loading failed",
                description = "There was an error loading a picture. Try again later",
                duration = StackedSnackbarDuration.Short
            )
        },
        fallback = placeHolder,
        placeholder = placeHolder
    )
}