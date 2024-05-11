package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.actions.ForwardButton
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.feature.welcome.extensions.toInt
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WelcomeFlow(coroutineScope: CoroutineScope,onBoardingScreen : (@Composable () -> Unit)? = null) {
    val pagerState = rememberPagerState { 3 + (onBoardingScreen != null).toInt() }

    val content: (@Composable BoxScope.() -> Unit)?  = onBoardingScreen?.let { _ ->
        @Composable {
            ForwardButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 24.dp,end = 24.dp),
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
        }
    }

    val modifier = Modifier.fillMaxSize()

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = pagerState.currentPage != 3,
        key = { it },
        pageContent = {
            when(it) {
                0 -> GenericWelcome(
                    modifier = modifier,
                    title = stringResource(Res.strings.features_open_source_title),
                    description = Res.strings.features_open_source_description.styleWith(
                        spanStyle = SpanStyle(color = Color(0xff07E9F8)),
                        Res.strings.features_open_source_description_p1,
                        Res.strings.features_open_source_description_p2,
                        Res.strings.features_open_source_description_p3
                    ),
                    currentPosition = it,
                    // TODO : change the icon for this
                    imageVector = Icons.Default.ArrowDropDown,
                    outerGradient = painterResource(Res.images.outer_circle_filled_with_blue_gradient),
                    innerGradient = painterResource(Res.images.inner_circle_filled_with_blue_gradient),
                    content = content
                )
                1 -> GenericWelcome(
                    modifier =  modifier,
                    title = stringResource(Res.strings.features_modern_ui_title),
                    description = Res.strings.features_modern_ui_description.styleWith(
                        spanStyle = SpanStyle(color = Color(0xff02C80A)),
                        Res.strings.features_modern_ui_description_p1,
                        Res.strings.features_modern_ui_description_p2,
                    ),
                    currentPosition = it,
                    // TODO : change the icon for this
                    imageVector = Icons.Default.ArrowDropDown,
                    outerGradient = painterResource(Res.images.outer_circle_filled_with_green_gradient),
                    innerGradient = painterResource(Res.images.inner_circle_filled_with_green_gradient),
                    content = content
                )
                2 -> GenericWelcome(
                    modifier =  modifier,
                    title = stringResource(Res.strings.features_free_spotify_alternative_title),
                    description = Res.strings.features_free_spotify_alternative_description.styleWith(
                        spanStyle = SpanStyle(color = Color(0xffC84902)),
                        Res.strings.features_free_spotify_alternative_description_p1,
                        Res.strings.features_free_spotify_alternative_description_p2,
                        Res.strings.features_free_spotify_alternative_description_p3
                    ),
                    currentPosition = it,
                    // TODO : change the icon for this
                    imageVector = Icons.Default.ArrowDropDown,
                    outerGradient = painterResource(Res.images.outer_circle_filled_with_orange_gradient),
                    innerGradient = painterResource(Res.images.inner_circle_filled_with_orange_gradient),
                    content = content
                )
                3 -> onBoardingScreen!!()
            }
        }
    )
}