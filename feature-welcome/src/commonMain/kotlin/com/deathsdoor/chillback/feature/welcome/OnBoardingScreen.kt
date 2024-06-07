@file:Suppress("LocalVariableName")

package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.deathsdoor.chillback.core.layout.AdaptiveLayout
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.feature.welcome.components.CenteredHorizontalDivider
import com.deathsdoor.chillback.feature.welcome.components.ChillbackAuthScreenDesktop
import com.deathsdoor.chillback.feature.welcome.components.ChillbackAuthScreenMobileLandscape
import com.deathsdoor.chillback.feature.welcome.components.ChillbackAuthScreenMobilePortrait
import com.deathsdoor.chillback.feature.welcome.components.ForgotPasswordScreenMobileLandscape
import com.deathsdoor.chillback.feature.welcome.components.ForgotPasswordScreenMobilePortrait
import com.deathsdoor.chillback.feature.welcome.components.TermsAndPolicyDisclaimer
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun MobileOnBoardingScreen(
    welcome: @Composable (NavController) -> Unit,
    auth: @Composable (NavController) -> Unit,
    forgotPassword: @Composable (NavController) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { welcome(navController) }

        navigation(startDestination = "auth",route = "auth_screen") {
            composable("auth"){ auth(navController) }
            composable("forgot_password") { forgotPassword(navController) }
        }
    }
}

private fun NavController.navigateToAuthScreen() = navigate("auth_screen")
internal fun NavController.navigateToForgotPassword() = navigate("forgot_passwrd")

@Composable
internal fun OnBoardingScreen(coroutineScope : CoroutineScope) {
    AdaptiveLayout(
        onMobilePortrait = {
            MobileOnBoardingScreen(
                welcome = {
                    // TODO : Background Image
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier= Modifier.weight(1f))

                        PortraitWelcomeScreen(
                            coroutineScope = coroutineScope,
                            navController = it
                        )
                    }
                },
                auth = {
                    ChillbackAuthScreenMobilePortrait(
                        navController = it,
                        coroutineScope = coroutineScope,
                    )
                },
                forgotPassword = {
                    ForgotPasswordScreenMobilePortrait(it)
                }
            )
        },
        onMobileLandscape = {
            MobileOnBoardingScreen(
                welcome = {
                    // TODO : Background Image
                    Row(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier= Modifier.weight(1f))

                        LandscapeWelcomeScreen(
                            modifier = Modifier.padding(start = 32.dp),
                            coroutineScope = coroutineScope,
                            navController = it
                        )
                    }
                },
                auth = {
                    ChillbackAuthScreenMobileLandscape(
                        navController = it,
                        coroutineScope = coroutineScope,
                    )
                },
                forgotPassword = {
                    ForgotPasswordScreenMobileLandscape(it)
                }
            )
        },
        onDesktop = {
            DesktopWelcomeScreen(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 32.dp),
                coroutineScope = coroutineScope,
            )
        }
    )
}

@Composable
private fun DesktopWelcomeScreen(
    modifier: Modifier = Modifier,
    coroutineScope : CoroutineScope,
) = ElevatedCard(modifier = modifier) {
    val _12 = 12.dp
    val _24 = 24.dp

    Row(
        modifier = Modifier.padding(_12),
        horizontalArrangement = Arrangement.spacedBy(_24),
        content = {
            OutlinedCard(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f)){
                WelcomeFlow(coroutineScope)
            }

            Spacer(modifier =  Modifier.fillMaxWidth(0.1f))

            ChillbackAuthScreenDesktop(coroutineScope = coroutineScope)
        }
    )
}

@Composable
private fun PortraitWelcomeScreen(
    modifier: Modifier= Modifier,
    coroutineScope : CoroutineScope,
    navController: NavController
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
                navController = navController
            )

            CenteredHorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(CircleShape)
            )

            SkipButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 24.dp),
                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                coroutineScope = coroutineScope,
            )
        }
    )
}

@Composable
private fun LandscapeWelcomeScreen(
    modifier: Modifier= Modifier,
    navController: NavController,
    coroutineScope : CoroutineScope,
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
                        navController = navController
                    )

                    SkipButton(
                        modifier = Modifier.padding(_12),
                        style = MaterialTheme.typography.labelMedium,
                        coroutineScope = coroutineScope,
                    )
                }
            )
        }
    )
}

@Composable
private fun WelcomeText(modifier: Modifier = Modifier,style : TextStyle) = Text(
    modifier = modifier,
    text = Res.strings.welcome_slogan.styleWith(
        spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
        Res.strings.welcome_slogan_p1,
        Res.strings.welcome_slogan_p2
    ),
    fontWeight = FontWeight.Bold,
    style = style,
    textAlign = TextAlign.Center
)

@Composable
private fun GetStartedButton(
    modifier: Modifier = Modifier,
    navController: NavController,
    style: TextStyle
) = Button(
    modifier = modifier,
    onClick = { navController.navigateToAuthScreen() },
    content = {
        Text(
            text = stringResource(Res.strings.get_started),
            style = style,
        )
    }
)

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    style : TextStyle = LocalTextStyle.current,
    coroutineScope : CoroutineScope,
) = ClickableText(
    modifier = modifier,
    text = Res.strings.skip_login.styleWith(
        spanStyle = SpanStyle(textDecoration = TextDecoration.Underline,color = MaterialTheme.colorScheme.primary)
    ),
    style = style,
    onClick = {
        coroutineScope.launch {
            ApplicationPreference.hasSkippedLogin.update(true)
        }
    }
)
