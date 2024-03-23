package com.deathsdoor.chillback.ui.screens.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.auth.ChillbackAuthScreen
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.components.auth.TermsAndPolicyDisclaimer
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.ui.components.layout.CenteredDivider
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.navigation.navigateToApp
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.google.firebase.auth.AuthResult

@Composable
fun ChillbackWelcomeScreen(navigationToApp : () -> Unit) = BackgroundImage(
    // TODO : Improve this background Image
    painter = painterResource(id= R.mipmap.application_logo),
    modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f),
    content = {
        val windowAdaptiveSize = LocalWindowAdaptiveSize.current
        when(windowAdaptiveSize.widthSizeClass) {
            WindowWidthSizeClass.Expanded , WindowWidthSizeClass.Medium -> when(windowAdaptiveSize.heightSizeClass) {
                WindowHeightSizeClass.Compact -> {
                    var isGettingStarted by remember { mutableStateOf(true) }

                    AnimatedContent(targetState = isGettingStarted) {
                        if(it) LandscapeWelcomeScreen(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(start = 32.dp),
                            navigateToLoginScreen = { isGettingStarted = false },
                            navigationToApp = navigationToApp
                        )
                        else ChillbackAuthScreen(
                            onBack = { isGettingStarted = true },
                            navigationToApp = navigationToApp,
                            onSuccess = onSuccessImpl(navigationToApp)
                        )
                    }
                }
                else -> DesktopWelcomeScreen(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp, vertical = 32.dp),
                    navigationToApp = navigationToApp,
                    windowAdaptiveSize = windowAdaptiveSize
                )
            }
            else -> {
                var isGettingStarted by remember { mutableStateOf(true) }

                AnimatedContent(targetState = isGettingStarted) {
                    if(it) PortraitWelcomeScreen(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        navigateToLoginScreen = { isGettingStarted = false },
                        navigationToApp = navigationToApp
                    )
                    else ChillbackAuthScreen(
                        onBack = { isGettingStarted = true },
                        navigationToApp = navigationToApp,
                        onSuccess = onSuccessImpl(navigationToApp)
                    )
                }
            }
        }
    }
)


@Suppress("LocalVariableName")
@Composable
private fun DesktopWelcomeScreen(
    modifier: Modifier = Modifier,
    navigationToApp: () -> Unit,
    windowAdaptiveSize : WindowSizeClass
) {
    val heightSizeClass = windowAdaptiveSize.heightSizeClass
    val widthSizeClass = windowAdaptiveSize.widthSizeClass
    val _32 = 32.dp
    val _24 = 24.dp
    val _16 = 16.dp
    val _12 = 12.dp
    val _8 = 8.dp

    val typography = MaterialTheme.typography
    val appLogoSize : Dp
    val appLogoSecondarySloganSpace : Dp
    val appSecondarySloganStyle : TextStyle
    val appReviewStyle : TextStyle

    when(heightSizeClass) {
        WindowHeightSizeClass.Medium -> {
            appLogoSize = 64.dp
            appLogoSecondarySloganSpace = 56.dp
            appSecondarySloganStyle = typography.labelSmall
            appReviewStyle = typography.labelSmall.copy(
                fontSize = typography.labelSmall.fontSize * 0.75,
                lineHeight =  typography.labelSmall.fontSize * 0.75
            )
        }
        WindowHeightSizeClass.Expanded -> {
            appLogoSize = 96.dp
            appLogoSecondarySloganSpace= 118.dp
            appSecondarySloganStyle = typography.titleSmall
            appReviewStyle = typography.labelSmall
        }
        else -> error("Unreachabled")
    }

    Card(modifier = modifier,elevation = CardDefaults.elevatedCardElevation()) {
        Row(
            modifier = Modifier.padding(_12),
            horizontalArrangement = Arrangement.spacedBy(_24),
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(if (widthSizeClass == WindowWidthSizeClass.Medium) 0.5f else 0.3f)
                        .fillMaxHeight(),
                    colors = CardDefaults.elevatedCardColors(),
                    content = {
                        Icon(
                            modifier = Modifier
                                .size(appLogoSize)
                                .padding(start = _32, top = _32),
                            painter = painterResource(id = R.mipmap.application_logo),
                            contentDescription = null
                        )

                        WelcomeText(
                            modifier = Modifier.padding(top = appLogoSecondarySloganSpace, bottom = _32, start = _8,end = _8),
                            style = appSecondarySloganStyle
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Card(
                            modifier = Modifier.padding(horizontal = _24,vertical = _24),
                            content = {
                                val colorScheme = MaterialTheme.colorScheme
                                Text(
                                    modifier = Modifier.padding(_16),
                                    text = buildAnnotatedString {
                                        // Title with bold style
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("This app is a ")
                                            withStyle(style = SpanStyle(color = colorScheme.primary)) {
                                                append("game changer! ")
                                            }
                                        }

                                        // Descriptive text with normal style
                                        append("It's like having a personal sound oasis in my pocket. ")

                                        // "Chill vibe" with italic style
                                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                            append("The ")

                                            withStyle(style = SpanStyle(color = colorScheme.primary)) {
                                                append("customizable options create exactly the chill vibe")
                                            }

                                            append("I need.")
                                        }

                                        append(" Plus, it's ")

                                        withStyle(style = SpanStyle(color = colorScheme.primary)) {
                                            append("super light and powerful - no lag")
                                        }

                                        append(" , just pure relaxation. Love it!")
                                    },
                                    style = if(widthSizeClass != WindowWidthSizeClass.Medium) appReviewStyle
                                    else appReviewStyle.copy(
                                        fontSize = appReviewStyle.fontSize * 1.25,
                                        lineHeight = appReviewStyle.lineHeight * 1.5
                                    )
                                )

                                Row(
                                    modifier = Modifier.padding(bottom = _8),
                                    verticalAlignment = Alignment.CenterVertically,
                                    content ={
                                        Icon(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(start = _16, end = _12),
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "By Anonymous",
                                            style = appReviewStyle,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }
                        )
                    }
                )


                ChillbackAuthScreen(
                    navigationToApp = navigationToApp,
                    onSuccess = onSuccessImpl(navigationToApp)
                )
            }
        )
    }
}

@Composable
private fun onSuccessImpl(navigationToApp: () -> Unit) = if(LocalInspectionMode.current) { _ -> navigationToApp() } else {
    val navController = LocalAppState.current.navController
    { it : AuthResult ->
        navController.navigateToApp()
        navigationToApp()
    }
}

@Composable
private fun LandscapeWelcomeScreen(
    modifier: Modifier= Modifier,
    navigateToLoginScreen : () -> Unit,
    navigationToApp : () -> Unit
) {
    val _70 = 70.dp
    val _16 = 16.dp
    val _12 = 12.dp
    val _8 = 8.dp

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = _70, bottomStart = _70),
        elevation = CardDefaults.elevatedCardElevation(),
        content = {
            WelcomeText(
                modifier = Modifier.padding(top = 32.dp, bottom = _16, start =_16,end = _8),
                style = MaterialTheme.typography.bodySmall
            )

            TermsAndPolicyDisclaimer(
                modifier = Modifier.padding(start = _16,bottom = _8,end = _8),
                imageModifier = Modifier
                    .size(48.dp)
                    .padding(end = _8),

                style = MaterialTheme.typography.labelSmall
            )

            Row(
                modifier = Modifier
                    .padding(start = _16)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    GetStartedButton(
                        modifier = Modifier
                            .padding(_12)
                            .weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        navigateToLoginScreen = navigateToLoginScreen
                    )

                    SkipButton(
                        modifier = Modifier.padding(_12),
                        style = MaterialTheme.typography.labelMedium,
                        navigationToApp = navigationToApp
                    )
                }
            )
        }
    )
}

@Composable
private fun PortraitWelcomeScreen(
    modifier: Modifier= Modifier,
    navigateToLoginScreen : () -> Unit,
    navigationToApp : () -> Unit
) {
    val _70 = 70.dp
    val _8 = 8.dp
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = _70,topEnd = _70),
        content = {
            WelcomeText(
                modifier = Modifier.padding(top = 56.dp, bottom = 32.dp, start = _8,end = _8),
                style = MaterialTheme.typography.headlineSmall
            )

            TermsAndPolicyDisclaimer(
                modifier = Modifier.padding(start = 16.dp,bottom = _8,end = _8),
                imageModifier = Modifier
                    .size(48.dp)
                    .padding(end = _8),
                style =MaterialTheme.typography.bodyMedium
            )

            GetStartedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                style = MaterialTheme.typography.titleLarge,
                navigateToLoginScreen = navigateToLoginScreen
            )

            CenteredDivider(modifier = Modifier
                .fillMaxWidth(0.7f)
                .clip(CircleShape))

            SkipButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 24.dp),
                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                navigationToApp = navigationToApp
            )
        }
    )
}

@Composable
private fun WelcomeText(modifier: Modifier = Modifier,style : TextStyle) = Text(
    modifier = modifier,
    text = styledText(
        plain0 = "Play your favorite tracks ",
        colored0 = "offline",
        plain1 = " with full customization on our app – from the latest hits to ",
        colored1 = "timeless classics!"
    ),
    fontWeight = FontWeight.Bold,
    style = style,
    textAlign = TextAlign.Center
)


@Composable
private fun GetStartedButton(
    modifier: Modifier = Modifier,
    navigateToLoginScreen : () -> Unit,
    style: TextStyle
) = Button(
    modifier = modifier,
    onClick = navigateToLoginScreen,
    content = {
        Text(
            text = "Get Started",
            style = style,
        )
    }
)

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    style : TextStyle,
    navigationToApp: () -> Unit,
) = ClickableText(
    modifier = modifier,
    text = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline,color = MaterialTheme.colorScheme.primary)) {
            append("Skip Login")
        }
    },
    style = style,
    onClick = { navigationToApp() }
)


@Preview
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@PreviewLightDark
@Composable
internal fun ChillbackWelcomeScreenPreview() {
    InitializeProvidersForPreview {
        ChillbackMaterialTheme {
            ChillbackWelcomeScreen {

            }
        }
    }
}