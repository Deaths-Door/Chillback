package com.deathsdoor.chillback.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.deathsdoor.chillback.core.layout.AdaptiveLayout
import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.feature.welcome.components.VerifyEmailDialog
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(coroutineScope : CoroutineScope) {
    VerifyEmailDialog(
        onVerified = {
            // Once we navigate to main screen it means don't show this again
            ApplicationPreference.hasSkippedLogin.update(true)
        },
        content = {
            val onBoardingScreen = @Composable {
                OnBoardingScreen(coroutineScope)
            }

            AdaptiveLayout(
                onMobile = {
                    WelcomeFlow(coroutineScope,onBoardingScreen)
                },
                onDesktop = onBoardingScreen
            )
        }
    )
}