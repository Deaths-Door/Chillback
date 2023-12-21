package com.deathsdoor.chillback.data.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deathsdoor.chillback.ui.screens.core.UserLibraryScreen

enum class AppScreenRoutes(
    val route : String,
    val icon : ImageVector,
    val content: @Composable (NavHostController) -> Unit
) {
    EventMaps("event-maps", Icons.Default.Info,{Box(Modifier.background(Color.Red).size(50.dp)) }),
    Explore("explore", Icons.Default.Info, {Box(Modifier.background(Color.Black).size(50.dp))}),
    UserLibrary("user-lib", Icons.Default.Info,{ UserLibraryScreen(it) }),
}