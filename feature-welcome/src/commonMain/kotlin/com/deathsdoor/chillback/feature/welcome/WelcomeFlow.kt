package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.actions.ForwardButton
import com.deathsdoor.chillback.core.layout.extensions.onHasFocusPreviewKeyEvent
import com.deathsdoor.chillback.core.layout.extensions.requestFocusOnClick
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.feature.welcome.components.GenericWelcome
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


    Column(
        modifier = Modifier.fillMaxSize()
            .onHasFocusPreviewKeyEvent {
                val offset = when {
                    it.key == Key.DirectionLeft && pagerState.currentPage != 0 -> -1
                    it.key == Key.DirectionRight && pagerState.currentPage != pagerState.pageCount -> 1
                    else -> return@onHasFocusPreviewKeyEvent false
                }

                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + offset)
                }

                false
            }.requestFocusOnClick()
        ,
        content ={
            val modifier = Modifier.fillMaxHeight()
            HorizontalPager(
                modifier = Modifier.weight(1f),
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
                            // TODO : change the icon for this
                            imageVector = Icons.Default.ArrowDropDown,
                            outerGradient = painterResource(Res.images.outer_circle_filled_with_blue_gradient),
                            innerGradient = painterResource(Res.images.inner_circle_filled_with_blue_gradient),
                            content = content
                        )
                        1 -> GenericWelcome(
                            modifier = modifier,
                            title = stringResource(Res.strings.features_modern_ui_title),
                            description = Res.strings.features_modern_ui_description.styleWith(
                                spanStyle = SpanStyle(color = Color(0xff02C80A)),
                                Res.strings.features_modern_ui_description_p1,
                                Res.strings.features_modern_ui_description_p2,
                            ),
                            // TODO : change the icon for this
                            imageVector = Icons.Default.ArrowDropDown,
                            outerGradient = painterResource(Res.images.outer_circle_filled_with_green_gradient),
                            innerGradient = painterResource(Res.images.inner_circle_filled_with_green_gradient),
                            content = content
                        )
                        2 -> GenericWelcome(
                            modifier = modifier,
                            title = stringResource(Res.strings.features_free_spotify_alternative_title),
                            description = Res.strings.features_free_spotify_alternative_description.styleWith(
                                spanStyle = SpanStyle(color = Color(0xffC84902)),
                                Res.strings.features_free_spotify_alternative_description_p1,
                                Res.strings.features_free_spotify_alternative_description_p2,
                                Res.strings.features_free_spotify_alternative_description_p3
                            ),
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

            CircleProgress(currentPosition = pagerState.currentPage)
        }
    )
}

@Composable
private fun ColumnScope.CircleProgress(currentPosition : Int) {
    val circleModifier = Modifier.clip(shape = CircleShape)
        .size(16.dp)

    Row(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            (0..2).forEach {
                val circleColor = if(currentPosition == it) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.inverseSurface
                Box(modifier = circleModifier.background(shape = CircleShape,color = circleColor))
            }
        }
    )
}