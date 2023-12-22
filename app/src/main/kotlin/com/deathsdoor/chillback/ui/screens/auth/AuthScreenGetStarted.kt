package com.deathsdoor.chillback.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.navigation.AuthScreenRoutes
import com.deathsdoor.chillback.ui.components.BackgroundImage
import com.deathsdoor.chillback.ui.components.CenteredDivider
import com.deathsdoor.chillback.ui.providers.LocalSettings
import kotlinx.coroutines.launch

@Composable
fun AuthScreenGetStarted(navController: NavController) = BackgroundImage(
    painter = painterResource(R.drawable.headphones),
    content = {
        Card(
            modifier = Modifier.align(Alignment.BottomCenter),

            shape = RoundedCornerShape(topStart = 70.dp,topEnd = 70.dp),
            content = {
                StyledText(modifier = Modifier.padding(top = 56.dp, bottom = 32.dp, start = 8.dp,end = 8.dp))

                GetStartedButton(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    navController = navController
                )

                CenteredDivider(modifier = Modifier.fillMaxWidth(0.7f).clip(CircleShape))

                SkipButton(modifier = Modifier.fillMaxWidth().padding(top = 12.dp,bottom = 24.dp))
            }
        )
    }
)

@Composable
private fun StyledText(modifier: Modifier = Modifier) = Text(
    modifier = modifier,
    text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Play your favorite tracks ")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("offline")
            }

            append(" with full customization on our\n")
            append("app – from the latest hits to ")

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("timeless classics!")
            }
        }
    },
    style = MaterialTheme.typography.headlineSmall,
    textAlign = TextAlign.Center
)

@Composable
private fun GetStartedButton(modifier: Modifier = Modifier,navController: NavController) = Button(
    modifier = modifier,
    onClick = { navController.navigate(AuthScreenRoutes.AuthOption.route) },
    content = {
        Text(
            text = "Get Started",
            style = MaterialTheme.typography.titleLarge,
        )
    }
)


@Composable
private fun SkipButton(modifier: Modifier = Modifier) {
    val settings = LocalSettings.current
    val coroutineScope = rememberCoroutineScope()

    ClickableText(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Skip")
            }
        },
        style = MaterialTheme.typography.titleMedium + TextStyle(textAlign = TextAlign.Center),
        onClick = {
            coroutineScope.launch {
                settings.updateHasSkippedLogin(true)
            }
        }
    )
}