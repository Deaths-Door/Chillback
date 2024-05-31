package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.chillback.core.media.components.action.ShareIcon
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.core.media.extensions.isOnDevice
import com.deathsdoor.chillback.core.media.extensions.shareMediaItem

@Composable
internal fun ShareMediaItemButton(mediaItem: AstroMediaItem,modifier: Modifier = Modifier) = IconButton(
    modifier = modifier,
    enabled = !mediaItem.isOnDevice(),
    onClick = { mediaItem.shareMediaItem() },
    content = {
        ShareIcon()
    }
)