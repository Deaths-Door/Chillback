package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.deathsdoor.chillback.feature.mediaplayer.icons.Lyrics
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun LyricsButton(navController: NavController,modifier : Modifier = Modifier) = IconButton(
    modifier = modifier,
    onClick = { navController.navigateToLyricsScreen() },
    content = {
        Icon(
            imageVector = Icons.Lyrics,
            contentDescription = stringResource(Res.strings.see_lyrics)
        )
    }
)

internal fun NavController.navigateToLyricsScreen() : Unit = TODO("implement this shit")