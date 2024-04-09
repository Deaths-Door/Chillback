package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.deathsdoor.chillback.ui.components.layout.MarqueeText

@NonRestartableComposable
@Composable
fun ErrorSnackbar(data: SnackbarData) = Toast(data, Color.Red)

@NonRestartableComposable
@Composable
fun SuccessSnackbar(data: SnackbarData) = Toast(data,Color.Green)

@NonRestartableComposable
@Composable
fun InfoSnackbar(data: SnackbarData) = Toast(data,Color.Blue)

@Composable
private fun Toast(data: SnackbarData,backgroundColor : Color) = Snackbar(
    snackbarData = data,
    shape = CircleShape,
    containerColor = backgroundColor,
)