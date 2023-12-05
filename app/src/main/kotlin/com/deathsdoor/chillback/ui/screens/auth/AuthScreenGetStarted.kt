package com.deathsdoor.chillback.ui.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.data.navigation.AuthScreenRoutes
import com.deathsdoor.chillback.ui.screens.providers.LocalSettings
import kotlinx.coroutines.launch

@Composable
fun AuthScreenGetStarted(navController: NavController) = BackgroundImage(
    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
    painter = painterResource(R.drawable.ic_launcher_background),
    content = {
        Card(
            modifier = Modifier.align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 70.dp,topEnd = 70.dp),
            content = {
                Spacer(modifier = Modifier.height(24.dp))

                // TODO : Style this , use AnnotatedString
                Text(
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 8.dp),
                    text = "Play your favorite tracks offline with full customization on our\napp – from the latest hits to timeless classics!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )


                Button(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    onClick = { navController.navigate(AuthScreenRoutes.AuthOption.route) },
                    content = {
                        Text(
                            text = "Get Started",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                )

                Divider()

                val settings = LocalSettings.current
                val coroutineScope = rememberCoroutineScope()

                ClickableText(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp,bottom = 24.dp),
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
        )
    }
)