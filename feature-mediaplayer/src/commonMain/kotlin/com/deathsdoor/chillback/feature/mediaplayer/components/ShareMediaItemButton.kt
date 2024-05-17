package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.io.files.SystemFileSystem

@Composable
internal fun ShareMediaItemButton(mediaItem: AstroMediaItem,modifier: Modifier = Modifier) = IconButton(
    modifier = modifier,
    enabled = !mediaItem.existsOnDevice(),
    onClick = {
         // TODO : Generate sharable link and share it
    },
    content = {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = stringResource(Res.strings.share)
        )
    }
)

private fun AstroMediaItem.existsOnDevice() : Boolean {
    return try {
        SystemFileSystem.exists(kotlinx.io.files.Path(source.toString()))
    } catch (exception :  kotlinx.io.IOException){
        false
    }
}