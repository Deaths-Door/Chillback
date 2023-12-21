package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.deathsdoor.chillback.ui.screens.auth.AuthScreenGetStarted
import com.deathsdoor.chillback.ui.screens.auth.AuthScreenOption

enum class AuthScreenRoutes(
    val route : String,
    val content: @Composable (NavHostController) -> Unit
) {
    GetStarted("get-started",{ AuthScreenGetStarted(it) }),
    AuthOption("auth-option",{ AuthScreenOption(it) }),
}