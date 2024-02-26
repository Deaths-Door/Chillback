package com.deathsdoor.chillback.ui.screens.welcome

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.deathsdoor.chillback.data.preferences.ApplicationSettings.Settings.Companion.update
import com.deathsdoor.chillback.ui.components.TermsAndPolicyDisclaimer
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.ui.components.layout.CenteredDivider
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch

@Composable
fun ChillbackWelcomeScreen(navigationToApp : () -> Unit) = BackgroundImage(
    painter = painterResource(id= R.drawable.ic_launcher_background),
    modifier = { Modifier.fillMaxWidth().aspectRatio(1f) },
    content = {
        Card(
            modifier = Modifier.align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 70.dp,topEnd = 70.dp),
            content = {
                WelcomeText(modifier = Modifier.padding(top = 56.dp, bottom = 32.dp, start = 8.dp,end = 8.dp))

                TermsAndPolicyDisclaimer(
                    modifier = Modifier.padding(start = 16.dp,bottom = 8.dp,end = 8.dp),
                    imageModifier = Modifier.size(48.dp).padding(end = 8.dp)
                )

                GetStartedButton(modifier = Modifier.fillMaxWidth().padding(12.dp))

                CenteredDivider(modifier = Modifier.fillMaxWidth(0.7f).clip(CircleShape))

                SkipButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp,bottom = 24.dp),
                    navigationToApp = navigationToApp
                )
            }
        )
    }
)

@Composable
private fun WelcomeText(modifier: Modifier = Modifier) = Text(
    modifier = modifier,
    text = styledText(
        plain0 = "Play your favorite tracks ",
        colored0 = "offline",
        plain1 = " with full customization on our\napp – from the latest hits to ",
        colored1 = "\"timeless classics!\""
    ),
    fontWeight = FontWeight.Bold,
    style = MaterialTheme.typography.headlineSmall,
    textAlign = TextAlign.Center
)


@Composable
private fun GetStartedButton(modifier: Modifier = Modifier) = Button(
    modifier = modifier,
    onClick = { TODO("NAVIGATE TO LOGIN") },
    content = {
        Text(
            text = "Get Started",
            style = MaterialTheme.typography.titleLarge,
        )
    }
)

@Composable
private fun SkipButton(modifier: Modifier = Modifier,navigationToApp: () -> Unit) = ClickableText(
    modifier = modifier,
    text = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("Skip Login")
        }
    },
    style = MaterialTheme.typography.titleMedium + TextStyle(textAlign = TextAlign.Center),
    onClick = { navigationToApp() }
)