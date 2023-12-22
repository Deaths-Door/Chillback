package com.deathsdoor.chillback.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(modifier: Modifier = Modifier,onClick : () -> Unit) = TextButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Image(
            modifier = Modifier.padding(16.dp),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Go Back"
        )
    }
)
