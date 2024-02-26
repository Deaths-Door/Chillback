package com.deathsdoor.chillback.ui.components.track

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.TrackDetails

// TODO: MAYBE ALLOW SAME PAINTER, SO IT CAN BE REMEMBERED -> MORE EFFICENT for lists
// TODO : FIX THIS SO IT load
@Composable
@NonRestartableComposable
fun Artwork(
    modifier : Modifier = Modifier,
    uri : Uri?
) = Image(
    modifier = modifier.clip(RoundedCornerShape(8.dp)),
    painter = uri?.let { rememberAsyncImagePainter(uri,onState = { Log.d("image","$it -> ${if(it is AsyncImagePainter.State.Error) it.result.throwable else ""} ") }) } ?: painterResource(id = R.drawable.ic_launcher_background),
    contentDescription = null,
)