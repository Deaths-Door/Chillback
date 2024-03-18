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
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.preferences.ApplicationSettings
import com.deathsdoor.chillback.data.preferences.ApplicationSettings.Settings.Companion.update
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

const val SPLASHSCREEN_DURATION = 2000L

@Composable
fun ChillbackSplashScreen(
    onBoardingContent : @Composable (navigationToApp : () -> Unit) -> Unit,
    appContent : @Composable () -> Unit,
) {
    var isMainScreen : Boolean? by remember { mutableStateOf(null) }

    val skippedLogin = when {
        LocalInspectionMode.current -> ApplicationSettings.Settings.dummyFalse
        else -> LocalAppState.current.settings.miscellaneous.skippedLogin
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(SPLASHSCREEN_DURATION)
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
            animationSpec = tween(durationMillis = SPLASHSCREEN_DURATION.toInt(), easing = FastOutLinearInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .applyIf(LocalDensity.current.fontScale == 2f) {
                padding(horizontal = 12.dp)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Image(
                modifier = Modifier.fillMaxSize(sizeAnimation.value),
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null
            )

            val typography =  MaterialTheme.typography
            val widthSizeClass = LocalWindowAdaptiveSize.current.widthSizeClass

            val appNameStyle: TextStyle
            val sloganStyle: TextStyle

            when(widthSizeClass) {
                WindowWidthSizeClass.Expanded -> {
                    appNameStyle = typography.displayLarge.copy(lineHeight =  MaterialTheme.typography.displayLarge.lineHeight * 2.5)
                    sloganStyle = typography.headlineSmall
                }
                else -> {
                    appNameStyle = typography.displayMedium
                    sloganStyle = typography.titleLarge
                }
            }

            Text(
                style = appNameStyle,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Chillback")
                    }
                }
            )
            
            Text(
                textAlign = TextAlign.Center,
                style = sloganStyle,
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

@Preview
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@PreviewLightDark
@Composable
internal fun ChillbackSplashScreenPreview() {
    InitializeProvidersForPreview {
        ChillbackMaterialTheme {
            ChillbackSplashScreen(
                onBoardingContent = { Text("We are boarding") },
                appContent = { Text("we can reached app") },
            )
        }
    }
}