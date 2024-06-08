package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.deathsdoor.chillback.feature.mediaplayer.icons.Equalizer
import com.deathsdoor.chillback.feature.mediaplayer.navigateToEqualizerScreen
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
@NonRestartableComposable
fun EqualizerButton(navController : NavController,modifier : Modifier = Modifier) = IconButton(
    modifier = modifier,
    onClick = { navController.navigateToEqualizerScreen() },
    content = {
        Icon(
            imageVector = Icons.Equalizer,
            contentDescription = stringResource(Res.strings.open_equalizer)
        )
    }
)