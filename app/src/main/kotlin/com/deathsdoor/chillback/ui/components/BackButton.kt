package com.deathsdoor.chillback.ui.components

import android.media.Image
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(onClick : () -> Unit) {
    TextButton(
        onClick = onClick,
        content = {
            Image(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back"
            )
        }
    )
}