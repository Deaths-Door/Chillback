package com.deathsdoor.chillback.ui.components.mediaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R

@Composable
fun TrackArtwork(modifier : Modifier = Modifier,mediaController : MediaController) {
    val artworkData = mediaController.currentMediaItem?.mediaMetadata?.artworkData?.asBitmap()

    if (artworkData == null) {
        Image(
            modifier = modifier,
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
        )

        return
    }

    Image(
        bitmap = artworkData.asImageBitmap(),
        modifier = modifier,
        contentDescription = null,
    )
}

fun ByteArray.asBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)