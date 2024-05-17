package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.core.layout.AdaptiveMobileLayout
import kotlinx.coroutines.launch

@Composable
internal fun ExpandedMediaPlayer(
    state : AstroPlayerState,
    onDismiss : () -> Unit
) = AdaptiveMobileLayout(
    onPortrait = { ExpandedMediaPlayerPortrait(state,onDismiss) },
    onLandscape = { ExpandedMediaPlayerLandscape(state,onDismiss) }
)

@Composable
private fun ExpandedMediaPlayerLandscape(
    state : AstroPlayerState,
    onDismiss : () -> Unit
) {
    val modifier = Modifier.fillMaxSize()

    Box(modifier) {
        var screenIndex by remember { mutableIntStateOf(0) }

        AnimatedContent(targetState = screenIndex) {
            when (it) {
                0 -> ExpandedFullScreenMediaPlayer(
                    state = state,
                    header = {
                        // TODO - Add this for show lyrics screen
                        ExpandedMediaPlayerHeader(
                            onDismiss = onDismiss,
                            onClick = { index ->
                                screenIndex = index
                            }
                        )
                    }
                )
                1 -> TODO("SHOW LYRICS")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpandedMediaPlayerPortrait(
    state : AstroPlayerState,
    onDismiss : () -> Unit
) = Column {
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    ExpandedMediaPlayerHeader(onDismiss = onDismiss) { index ->
        coroutineScope.launch { pagerState.animateScrollToPage(index) }
    }

    HorizontalPager(
        state = pagerState,
        pageContent = {
            when(it) {
                0 -> ExpandedMediaPlayerPlayingScreenPortrait(state)
                1 -> TODO("SHOW LYRICS")
            }
        }
    )
}