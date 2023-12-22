package com.deathsdoor.chillback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.deathsdoor.chillback.ui.providers.InitializeProviders
import com.deathsdoor.chillback.ui.providers.LocalSettings
import com.deathsdoor.chillback.ui.screens.AppScreen
import com.deathsdoor.chillback.ui.screens.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InitializeProviders {
                val showLoginScreen by LocalSettings.current.showLoginScreen.collectAsState(true)

                SplashScreen {
                    AppScreen(showLoginScreen)
                }
            }
        }
    }
}