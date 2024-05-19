package com.deathsdoor.chillback.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.astroplayer.ui.rememberAstroPlayerState
import com.deathsdoor.chillback.core.layout.AdaptiveLayout
import com.deathsdoor.chillback.core.layout.navigationsuite.ScaffoldSuite
import com.deathsdoor.chillback.data.rememberChillbackState
import com.deathsdoor.chillback.feature.mediaplayer.ExpandableMediaPlayer
import com.deathsdoor.chillback.feature.mediaplayer.FullScreenMediaPlayer
import com.deathsdoor.chillback.feature.mediaplayer.LargeMediaPlayerBar

@Composable
fun ChillbackApplication(applicationNavController : NavController,content : @Composable (modifier : Modifier) -> Unit) {
    val chillbackState = rememberChillbackState()
    val astroPlayerState = rememberAstroPlayerState(chillbackState.astroPlayer)

    ScaffoldSuite(
        scaffoldSuiteItems = {
            // TODO : Add content over here
        },
        content = { paddingValues ->
            AdaptiveLayout(
                onMobile = {
                    Box(
                        modifier = Modifier
                            .padding(paddingValues!!)
                            .fillMaxSize(),
                        content = {
                            content(Modifier.matchParentSize())

                            ExpandableMediaPlayer(
                                state = astroPlayerState,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    )
                },
                onDesktop = {
                    val fillMaxSize = Modifier.fillMaxSize()

                    FullScreenMediaPlayer(
                        modifier = fillMaxSize,
                        state = astroPlayerState,
                        applicationNavController = applicationNavController,
                        content = {
                            Column(modifier = fillMaxSize) {
                                content(Modifier.weight(1f))

                                LargeMediaPlayerBar(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    state = astroPlayerState,
                                    navController = it,
                                    applicationNavController = applicationNavController,
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}