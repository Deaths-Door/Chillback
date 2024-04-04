package com.deathsdoor.chillback.ui.components.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundButton
import com.deathsdoor.chillback.ui.extensions.themeBasedTint

@Composable
@NonRestartableComposable
fun DownButton(modifier: Modifier = Modifier, onClick : () -> Unit) = CircularBackgroundButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            modifier = Modifier.rotate(-90F),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Go Back",
            tint = themeBasedTint()
        )
    }
)
