package com.deathsdoor.chillback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.deathsdoor.chillback.ui.ChillbackApp
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.ChillbackSplashScreen
import com.deathsdoor.chillback.ui.providers.InitializeProviders
import com.deathsdoor.chillback.ui.screens.welcome.ChillbackWelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitializeProviders(this) {
                ChillbackMaterialTheme{
                    ChillbackSplashScreen(
                        onBoardingContent = { ChillbackWelcomeScreen(navigationToApp = it) },
                        appContent = { ChillbackApp() }
                    )
                }
            }
        }
    }
}