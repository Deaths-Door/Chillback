package com.deathsdoor.chillback.core.media.components.action

import RadioButtonUnchecked
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
fun SelectedIcon(modifier : Modifier = Modifier,isSelected: Boolean) = if (isSelected) {
    Icon(
        imageVector = Icons.Filled.CheckCircle,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = stringResource(Res.strings.is_selected),
        modifier = modifier
    )
} else {
    Icon(
        imageVector = Icons.RadioButtonUnchecked,
        tint = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.7f),
        contentDescription = stringResource(Res.strings.is_not_selected),
        modifier = modifier
    )
}