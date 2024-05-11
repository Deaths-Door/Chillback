package com.deathsdoor.chillback.core.layout.actions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@NonRestartableComposable
fun BackButton(modifier: Modifier = Modifier,onClick : () -> Unit) = FloatingActionButton(
    modifier = modifier.padding(start = 12.dp,top = 12.dp),
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
        )
    }
)

@Composable
@NonRestartableComposable
fun ForwardButton(modifier: Modifier = Modifier,onClick : () -> Unit) = FloatingActionButton(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null
        )
    }
)