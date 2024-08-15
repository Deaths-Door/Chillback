package com.deathsdoor.chillback.core.media.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DraggableBar(progress : MutableFloatState) {
    val interactionSource = remember { MutableInteractionSource() }

    Slider(
        value = progress.floatValue,
        onValueChange = { progress.floatValue = it },
        // no track
        track = {},
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = interactionSource,
                modifier = Modifier.height(IntrinsicSize.Max)
                    .padding(vertical = 12.dp)
            )
        }
    )
}