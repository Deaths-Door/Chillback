package com.deathsdoor.chillback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.deathsdoor.chillback.ui.screens.core.AppScreen
import com.deathsdoor.chillback.ui.screens.SplashScreen
import com.deathsdoor.chillback.ui.screens.auth.AuthScreen
import com.deathsdoor.chillback.ui.screens.providers.InitializeProviders
import com.deathsdoor.chillback.ui.screens.providers.LocalSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitializeProviders {
                // So the result is 'gotten' before so no visual issue
                val hasSkippedLogin by LocalSettings.current.hasSkippedLogin.collectAsState(false)
                SplashScreen {
                    if(hasSkippedLogin) AppScreen() else AuthScreen()
                }
            }
        }
    }
}