package com.deathsdoor.chillback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.deathsdoor.chillback.ui.screens.SplashScreen
import com.deathsdoor.chillback.ui.screens.auth.AuthScreen
import com.deathsdoor.chillback.ui.screens.providers.InitializeProviders
import com.deathsdoor.chillback.ui.screens.providers.LocalSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitializeProviders {
                SplashScreen {
                    val hasSkippedLogin by LocalSettings.current.hasSkippedLogin.collectAsState(false)
                    if(hasSkippedLogin) { TODO() } else AuthScreen()
                }
            }
        }
    }
}