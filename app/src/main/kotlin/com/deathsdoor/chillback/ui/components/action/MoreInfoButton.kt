package com.deathsdoor.chillback.ui.components.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoreInfoButton(modifier: Modifier = Modifier, onClick : () -> Unit) = IconButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options"
        )
    }
)

