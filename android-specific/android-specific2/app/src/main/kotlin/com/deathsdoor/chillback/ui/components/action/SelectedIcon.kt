package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R

@Composable
fun SelectedIcon(modifier : Modifier = Modifier,isSelected: Boolean) = if (isSelected) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    Icon(
        imageVector = Icons.Filled.CheckCircle,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = "Is Selected",
        modifier = modifier
            .border(2.dp, backgroundColor, CircleShape)
            .clip(CircleShape)
            .background(backgroundColor)
    )
} else {
    Icon(
        painter = painterResource(id = R.drawable.radio_button_unchecked),
        tint = Color.White.copy(alpha = 0.7f),
        contentDescription = "Is not selected",
        modifier = modifier
    )
}