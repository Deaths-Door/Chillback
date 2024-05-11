package com.deathsdoor.chillback.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.extensions.applyOn
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.core.resources.Res
import com.deathsdoor.chillback.feature.welcome.WelcomeScreen
import com.deathsdoor.chillback.feature.welcome.hasSkippedLogin
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

const val SPLASHSCREEN_DURATION = 2000L

@Composable
fun ChillbackSplashScreen(content : @Composable () -> Unit) {
    var isMainScreen : Boolean? by remember { mutableStateOf(null) }

    val skippedLogin = ApplicationPreference.hasSkippedLogin
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(SPLASHSCREEN_DURATION)
        isMainScreen = skippedLogin.current().first()
    }

    AnimatedContent(isMainScreen) {
        when (it){
            null -> SplashScreen()
            false -> WelcomeScreen(coroutineScope)
            true -> content()
        }
    }
}
@Composable
private fun SplashScreen() {
    val sizeAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        sizeAnimation.animateTo(
            targetValue = 1.5f,
            animationSpec = tween(durationMillis = SPLASHSCREEN_DURATION.toInt(), easing = FastOutLinearInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .applyOn(LocalDensity.current.fontScale == 2f) {
                padding(horizontal = 12.dp)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Image(
                modifier = Modifier.fillMaxSize(sizeAnimation.value),
                painter = painterResource(Res.images.application_logo),
                contentDescription = null
            )

            Text(
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                text = Res.strings.app_name.styleWith(spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary))
            )

            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                text = Res.strings.app_slogan.styleWith(
                    spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
                    Res.strings.app_slogan_p1
                )
            )
        }
    )
}