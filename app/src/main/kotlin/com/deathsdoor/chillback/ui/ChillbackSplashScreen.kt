package com.deathsdoor.chillback.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.deathsdoor.chillback.data.preferences.ApplicationSettings.Settings.Companion.update
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

const val SPLASHSCREEN_DURATION = 2000

@Composable
fun ChillbackSplashScreen(
    onBoardingContent : @Composable (navigationToApp : () -> Unit) -> Unit,
    appContent : @Composable () -> Unit
) {
    var isMainScreen : Boolean? by remember { mutableStateOf(null) }
    val skippedLogin = LocalAppState.current.settings.miscellaneous.skippedLogin
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(SPLASHSCREEN_DURATION.toLong())
        isMainScreen = skippedLogin.current().first()
    }

    AnimatedContent(isMainScreen) {
        when (it){
            null -> SplashScreen()
            false -> onBoardingContent {
                isMainScreen = true

                // Once we navigate to main screen it means don't show this again
                coroutineScope.update(skippedLogin,true)
            }
            true -> appContent()
        }
    }
}

@Composable
private fun SplashScreen() {
    val sizeAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        sizeAnimation.animateTo(
            targetValue = 1.5f,
            animationSpec = tween(durationMillis = SPLASHSCREEN_DURATION, easing = FastOutLinearInEasing)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Image(
                modifier = Modifier.fillMaxSize(sizeAnimation.value),
                imageVector = Icons.Default.Email,
                contentDescription = null
            )

            Text(
                style = MaterialTheme.typography.displayMedium,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Chillback")
                    }
                }
            )

            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                text = buildAnnotatedString {
                    append("Without ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Music\nLife")
                    }
                    append(" would be a mistake")
                }
            )
        }
    )
}