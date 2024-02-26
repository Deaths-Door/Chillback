package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.R

@Composable
fun RepeatMediaItemsButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    val repeatMode : @Player.RepeatMode Int by remember(mediaController.repeatMode) { mutableIntStateOf(mediaController.repeatMode)  }

    IconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if(repeatMode == REPEAT_MODE_OFF) LocalContentColor.current else MaterialTheme.colorScheme.primary,
        ),
        onClick = { mediaController.repeatMode = (mediaController.repeatMode + 1) % 3 },
        content = {
            Icon(
                painter = painterResource(if(repeatMode == REPEAT_MODE_ONE) R.drawable.repeat_one else R.drawable.repeat) ,
                contentDescription = when(repeatMode) {
                    REPEAT_MODE_OFF -> "Turn off repeat"
                    REPEAT_MODE_ONE ->"Repeat One Track"
                    REPEAT_MODE_ALL -> "Repeat All"
                    else -> throw IllegalArgumentException("This is unreachable")
                }
            )
        }
    )
}