package com.deathsdoor.chillback.ui.components.mediaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.deathsdoor.chillback.R

@Composable
fun TrackArtwork(
    modifier : Modifier = Modifier,
    currentMediaItem : MediaItem
) {
    @Suppress("NAME_SHADOWING")
    val modifier = modifier.clip(shape = RoundedCornerShape(8.dp))
    val artworkData = currentMediaItem.mediaMetadata.artworkData?.asBitmap()

    if (artworkData == null) {
        Image(
            modifier = modifier,
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
        )

        return
    }

    Image(
        modifier = modifier,
        bitmap = artworkData.asImageBitmap(),
        contentDescription = null,
    )
}

fun ByteArray.asBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)