package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.homepage.utils.PROJECT_HOME_PAGE

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun MainContent(headerHeight : Dp) = Row {
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }

    Column(modifier = Modifier.height(screenHeight - headerHeight)) {
        Spacer(modifier = Modifier.weight(1f))

        val chillbackStyle = MaterialTheme.typography.displayLarge

        Text(
            text = "Chillback",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            style = chillbackStyle.copy(fontSize = chillbackStyle.fontSize * 1.5)
        )

        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            style = MaterialTheme.typography.displayMedium,
            text = buildAnnotatedString {
                append("Without ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Music\nLife")
                }
                append(" would be a mistake")
            }
        )


        val textStyle = MaterialTheme.typography.headlineSmall

        Row(modifier = Modifier.padding(bottom = 32.dp),horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val importantButtonColors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            )
            Button(
                colors = importantButtonColors,
                onClick = { /*TODO : Scroll to download*/ },
                content = { Text(text = "Download",style = textStyle) }
            )


            val uriHandler = LocalUriHandler.current

            Button(
                onClick = { uriHandler.openUri(PROJECT_HOME_PAGE) },
                content = { Text(text = "Github",style = textStyle) }
            )

            Button(
                colors = importantButtonColors,
                onClick = { /*TODO : Navigate to login screen / if logged in the actual app*/ },
                content = { Text(text = "Try Out",style = textStyle) }
            )
        }
    }
}
