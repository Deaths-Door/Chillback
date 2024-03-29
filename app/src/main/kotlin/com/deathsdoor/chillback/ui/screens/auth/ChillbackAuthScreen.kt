package com.deathsdoor.chillback.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.components.action.BackButton
import com.deathsdoor.chillback.ui.components.auth.AuthButton
import com.deathsdoor.chillback.ui.components.auth.EmailOutlinedTextField
import com.deathsdoor.chillback.ui.components.auth.ForgotPassword
import com.deathsdoor.chillback.ui.components.auth.FullAlternativeAuthOptions
import com.deathsdoor.chillback.ui.components.auth.PasswordOutlinedTextField
import com.deathsdoor.chillback.ui.components.auth.ShortenAlternativeAuthOptions
import com.deathsdoor.chillback.ui.components.auth.TermsAndPolicyDisclaimer
import com.deathsdoor.chillback.ui.components.auth.ToggleAuthMode
import com.deathsdoor.chillback.ui.components.layout.CenteredDivider
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.deathsdoor.chillback.ui.screens.welcome.SkipButton
import com.deathsdoor.chillback.ui.state.AuthState
import com.google.firebase.auth.AuthResult

@Composable
fun ChillbackAuthScreen(
    onBack: (() -> Unit)? = null,
    onSuccess: (AuthResult) -> Unit,
    navigationToApp: () -> Unit
) {
    val authState = remember { AuthState() }
    val coroutineScope = rememberCoroutineScope()
    val windowAdaptiveSize= LocalWindowAdaptiveSize.current
    val widthSizeClass = windowAdaptiveSize.widthSizeClass
    val heightSizeClass = windowAdaptiveSize.heightSizeClass


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        content = {
            onBack?.let {
                BackButton(modifier = Modifier.offset(x = (-16).dp),onClick = it)
            }

            val titleStyle : TextStyle
            val subtitleStyle : TextStyle
            val textFieldStyle : TextStyle
            val authButtonStyle : TextStyle
            when(heightSizeClass) {
                WindowHeightSizeClass.Medium -> {
                    titleStyle = MaterialTheme.typography.titleMedium
                    subtitleStyle = MaterialTheme.typography.bodySmall
                    textFieldStyle = if(widthSizeClass != WindowWidthSizeClass.Compact) MaterialTheme.typography.labelSmall
                    else MaterialTheme.typography.labelSmall.copy(fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.25)

                    authButtonStyle = MaterialTheme.typography.labelMedium
                }
                else -> {
                    titleStyle = MaterialTheme.typography.headlineLarge
                    subtitleStyle = MaterialTheme.typography.titleMedium
                    textFieldStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * 0.5f)
                    authButtonStyle = MaterialTheme.typography.titleLarge
                }
            }
            Text(
                modifier = Modifier.padding(top = 56.dp),//, bottom = 24.dp),
                text = "Let's get you in",
                fontWeight = FontWeight.Bold,
                style = titleStyle,
            )

            AnimatedContent(targetState = authState.isLoginShown) {
                if(it) Text(
                    text = "Login into your account now",
                    style = subtitleStyle,
                )
                else Text(
                    text = "Create your account now",
                    style = subtitleStyle,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

           // Text("Email")
            authState.EmailOutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                style = textFieldStyle
            )

            authState.PasswordOutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                style = textFieldStyle
            )

            if(!authState.isLoginShown && widthSizeClass == WindowWidthSizeClass.Expanded) {
                TermsAndPolicyDisclaimer(
                    imageModifier = Modifier.size(48.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            val alignCenter =Modifier.align(Alignment.CenterHorizontally)
            authState.AuthButton(
                modifier = alignCenter
                    .fillMaxWidth(if (windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Expanded) 1f else 0.7f)
                    .padding(16.dp),
                coroutineScope = coroutineScope,
                style = authButtonStyle,
                navigationToApp = navigationToApp
            )

            when(widthSizeClass) {
                WindowWidthSizeClass.Expanded -> {
                    val style = if(heightSizeClass == WindowHeightSizeClass.Expanded) MaterialTheme.typography.bodySmall
                        else MaterialTheme.typography.labelSmall

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        ForgotPassword(
                            style = style,
                            coroutineScope = coroutineScope
                        )

                        authState.ToggleAuthMode(style = style)
                    }

                    FullAlternativeAuthOptions(
                        modifier = Modifier.padding(top = 12.dp),
                        coroutineScope = coroutineScope,
                        onSuccess = onSuccess,
                        style = if(heightSizeClass == WindowHeightSizeClass.Medium) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge
                    )

                    SkipButton(
                        modifier = alignCenter,
                        style = style,
                        navigationToApp = navigationToApp
                    )
                }
                else -> {
                    AnimatedVisibility(authState.isLoginShown) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ForgotPassword(
                                modifier= Modifier.padding(bottom = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                coroutineScope = coroutineScope
                            )

                            CenteredDivider(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .padding(top = 16.dp, bottom = 24.dp)
                                    .clip(CircleShape)
                            )

                            ShortenAlternativeAuthOptions(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                coroutineScope = coroutineScope,
                                onSuccess = onSuccess
                            )
                        }
                    }

                    authState.ToggleAuthMode(modifier = alignCenter.padding(top = 16.dp))
                    // TODO : add image in right corner
                }
            }
        }
    )
}



@Preview
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@PreviewLightDark
@Composable
internal fun ChillbackAuthScreenPreview() {
    InitializeProvidersForPreview {
        ChillbackMaterialTheme {
            ChillbackAuthScreen({},{},{})
        }
    }
}