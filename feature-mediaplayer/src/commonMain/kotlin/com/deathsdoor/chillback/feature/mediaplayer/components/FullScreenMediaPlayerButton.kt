package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.deathsdoor.chillback.feature.mediaplayer.icons.ZoomInwards
import com.deathsdoor.chillback.feature.mediaplayer.icons.ZoomPan
import com.deathsdoor.chillback.feature.mediaplayer.navigateToFullScreenMediaPlayer
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
@NonRestartableComposable
internal fun FullScreenMediaPlayerButton(
    navController: NavController,
    modifier : Modifier = Modifier
) = IconButton(
    modifier = modifier,
    onClick = { navController.navigateToFullScreenMediaPlayer() },
    content = {
        Icon(
            imageVector = Icons.ZoomPan,
            contentDescription = stringResource(Res.strings.open_fullscreen_mediaplayer)
        )
    }
)

@Composable
@NonRestartableComposable
internal fun CloseFullScreenMediaPlayerButton(
    navController: NavController,
    modifier : Modifier = Modifier
) = IconButton(
    modifier = modifier,
    onClick = { navController.popBackStack() },
    content = {
        Icon(
            imageVector = Icons.ZoomInwards,
            contentDescription = stringResource(Res.strings.open_fullscreen_mediaplayer)
        )
    }
)