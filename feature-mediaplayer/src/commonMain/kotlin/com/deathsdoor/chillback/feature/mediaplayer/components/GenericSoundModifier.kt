package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource


// https://play-lh.googleusercontent.com/JBIoWkTD37iaB44BY8NuKnsIuirhSCWujkLzfh_zBAFBqFWF6ARDs3V4SYsFYft3qf8=w5120-h2880-rw
// visually the loudness etc things
@Composable()
internal fun GenericSoundModifier(
    modifier : Modifier = Modifier,
    label : StringResource
) = Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)){
    Box {
        // TODO; semi circular slider
        Text(text = "Unknown")
    }

    Switch(
        checked = false,
        onCheckedChange = null
    )

    Text(text = stringResource(label),fontWeight = FontWeight.Bold)
}