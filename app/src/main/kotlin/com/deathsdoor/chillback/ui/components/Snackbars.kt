package com.deathsdoor.chillback.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.deathsdoor.chillback.ui.components.layout.MarqueeText

@NonRestartableComposable
@Composable
fun ErrorSnackbar(message: String) = Toast(message, Icons.Default.Share, Color.Red)

@NonRestartableComposable
@Composable
fun SuccessSnackbar(message: String) = Toast(message,Icons.Default.Notifications,Color.Green)

@NonRestartableComposable
@Composable
fun InfoSnackbar(message: String) = Toast(message,Icons.Default.Info,Color.Blue)

@Composable
private fun Toast(message : String, imageVector: ImageVector, backgroundColor : Color) = Snackbar(
    shape = CircleShape,
    containerColor = backgroundColor,
    content = {
        Row {
            Image(imageVector = imageVector,contentDescription = null)
            MarqueeText(text = message)
        }
    }
)