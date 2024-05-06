package com.deathsdoor.chillback.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources._app_slogan_colored_section
import chillback.`feature-welcome`.generated.resources.app_slogan
import chillback.`feature-welcome`.generated.resources.download
import chillback.`feature-welcome`.generated.resources.try_out
import com.deathsdoor.chillback.core.layout.extensions.PROJECT_HOME_PAGE
import com.deathsdoor.chillback.core.layout.extensions.asAnnotatedStringWithSpanStyle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
@Composable
internal fun MainContent(headerHeight: Dp, scrollState: LazyListState) = Row {
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }

    Column(modifier = Modifier.height(screenHeight - headerHeight)) {
        Spacer(modifier = Modifier.weight(1f))

        val chillbackStyle = MaterialTheme.typography.displayLarge
        val primary = MaterialTheme.colorScheme.primary
        Text(
            text = "Chillback",
            fontWeight = FontWeight.Bold,
            color = primary,
            style = chillbackStyle.copy(fontSize = chillbackStyle.fontSize * 1.5)
        )

        val appSloganColored = stringResource(Res.string._app_slogan_colored_section)

        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            style = MaterialTheme.typography.displayMedium,
            text = stringResource(
                Res.string.app_slogan,
                appSloganColored
            ).asAnnotatedStringWithSpanStyle(
                substring = appSloganColored,
                style = SpanStyle(
                    color = primary
                )
            )
        )


        val textStyle = MaterialTheme.typography.headlineSmall

        Row(modifier = Modifier.padding(bottom = 32.dp),horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val importantButtonColors = ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = MaterialTheme.colorScheme.surface
            )

            val coroutineScope = rememberCoroutineScope()

            Button(
                colors = importantButtonColors,
                onClick = {
                    coroutineScope.launch {
                        // Download is the 4 element in the list
                        scrollState.animateScrollToItem(index = 4)
                    }
                },
                content = { Text(text = stringResource(Res.string.download),style = textStyle) }
            )

            val uriHandler = LocalUriHandler.current

            Button(
                onClick = { uriHandler.openUri(PROJECT_HOME_PAGE) },
                content = { Text(text = "Github",style = textStyle) }
            )


            when(val platform = userPlatform()){
                "unknown" , "macos" -> Text(text ="This platform is not support", fontWeight = FontWeight.Bold)
                else -> Button(
                    colors = importantButtonColors,
                    onClick = {
                        when(platform) {
                            "android" -> uriHandler.openUri(ANDROID_LINK)
                            "ios" -> uriHandler.openUri(ANDROID_LINK)
                            "windows" , "linux" -> TODO("Support Link and Windows Download")
                        }
                    },
                    content = { Text(text = stringResource(Res.string.try_out),style = textStyle) }
                )
            }

        }
    }
}

