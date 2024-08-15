package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.media.extensions.optionsItemSpacing
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.deathsdoor.chillback.core.media.icons.AddToQueue
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
internal fun AddToQueueThumbItem(astroPlayer : AstroPlayer,mediaItem : AstroMediaItem) {
    val label = stringResource(Res.strings.add_to_queue)

    Thumbnail(
        title = { ThumbnailTitle(text = label) },
        modifier = Modifier
            .clickable(onClickLabel = label) {
                astroPlayer.addMediaItem(mediaItem)
            }
            .optionsItemSpacing(),
        artwork = {
            Icon(
                imageVector = Icons.AddToQueue,
                contentDescription = label,
            )
        }
    )
}

@Composable
internal fun AddToQueueDropDownItem(astroPlayer : AstroPlayer,mediaItem : AstroMediaItem) {
    val label = stringResource(Res.strings.add_to_queue)

    DropdownMenuItem(
        text = { Text(label) },
        onClick = { astroPlayer.addMediaItem(mediaItem) },
        leadingIcon = {
            Icon(
                imageVector = Icons.AddToQueue,
                contentDescription = label,
            )
        }
    )
}
