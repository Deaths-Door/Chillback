package com.deathsdoor.chillback.feature.mediaplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.resources.Res
import com.deathsdoor.chillback.feature.mediaplayer.components.CloseFullScreenMediaPlayerButton
import com.deathsdoor.chillback.feature.mediaplayer.components.ExpandedFullScreenMediaPlayer
import com.deathsdoor.chillback.feature.mediaplayer.components.UpNextMediaItem
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun FullScreenMediaPlayer(
    modifier : Modifier,
    state : AstroPlayerState,
    content : @Composable (NavHostController) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "app",
        builder = {
            composable("app") { content(navController) }
            composable("fullscreen_mediaplayer") {
                ExpandedFullScreenMediaPlayer(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    header = {
                        Row(modifier = it, horizontalArrangement = Arrangement.SpaceBetween) {
                            // For 'Disabled Effect`
                            val disabledColor = LocalContentColor.current.copy(alpha = 0.38f)

                            Icon(
                                modifier = Modifier.size(56.dp).padding(end = 12.dp),
                                painter = painterResource(Res.images.application_logo),
                                tint = disabledColor,
                                contentDescription = null
                            )

                            Text(
                                text = stringResource(com.deathsdoor.chillback.feature.mediaplayer.resources.Res.strings.playing_from_collection),
                                color = disabledColor,
                                style = MaterialTheme.typography.titleMedium
                            )

                            val showUpNext by remember {
                                derivedStateOf {
                                    (state.astroPlayer.contentDuration - state.astroPlayer.currentPosition) < 30
                                }
                            }

                            if(showUpNext) return@ExpandedFullScreenMediaPlayer

                            UpNextMediaItem(
                                modifier = Modifier.height(80.dp),
                                mediaItem = state.currentMediaItem!!
                            )
                        }
                    },
                    action = {
                        CloseFullScreenMediaPlayerButton(
                            modifier = Modifier.size(72.dp),
                            navController = navController
                        )
                    }
                )
            }
        }
    )
}

internal fun NavController.navigateToFullScreenMediaPlayer() = navigate("fullscreen_mediaplayer")