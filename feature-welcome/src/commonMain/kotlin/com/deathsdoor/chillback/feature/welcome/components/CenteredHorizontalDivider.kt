package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
internal fun CenteredHorizontalDivider(modifier: Modifier = Modifier) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    content = {
        HorizontalDivider(
            modifier = modifier,
            thickness = 4.dp
        )
    }
)