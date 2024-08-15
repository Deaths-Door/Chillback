package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.media.extensions.optionsItemSpacing
import com.deathsdoor.chillback.core.media.extensions.shareMediaItem
import com.deathsdoor.chillback.core.media.icons.AddToQueue
import com.deathsdoor.chillback.core.media.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ShareIcon(contentDescription : String = stringResource(Res.strings.share)) = Icon(
    imageVector = Icons.Default.Share,
    contentDescription = contentDescription
)

@Composable
internal fun ShareThumbItem(mediaItem : AstroMediaItem) {
    val label =stringResource(Res.strings.share)

    Thumbnail(
        title = { ThumbnailTitle(text = label) },
        modifier = Modifier
            .clickable(onClickLabel = label) {
                mediaItem.shareMediaItem()
            }
            .optionsItemSpacing(),
        artwork = { ShareIcon(label) }
    )
}

@Composable
internal fun ShareDropDownItem(mediaItem : AstroMediaItem) {
    val label = stringResource(Res.strings.share)

    DropdownMenuItem(
        text = { Text(label) },
        onClick = { mediaItem.shareMediaItem() },
        leadingIcon = { ShareIcon(label) }
    )
}
