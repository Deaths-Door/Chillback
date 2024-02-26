package com.deathsdoor.chillback.ui.components.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundImageVectorButton


@Composable
@NonRestartableComposable
fun BackButton(modifier: Modifier = Modifier,onClick : () -> Unit) = CircularBackgroundImageVectorButton(
    modifier = modifier,
    onClick = onClick,
    imageVector = Icons.Default.ArrowBack,
    contentDescription = "Go Back"
)
