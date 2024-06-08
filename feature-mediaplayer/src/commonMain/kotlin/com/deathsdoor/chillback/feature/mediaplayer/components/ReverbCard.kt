package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

// use design from https://play-lh.googleusercontent.com/JBIoWkTD37iaB44BY8NuKnsIuirhSCWujkLzfh_zBAFBqFWF6ARDs3V4SYsFYft3qf8=w5120-h2880-rw
@Composable
internal fun ReverbCard(modifier: Modifier = Modifier,enabled : Boolean) {
    var isReverbEnabled by remember(enabled) { mutableStateOf(if(enabled) false else enabled) }

    val snackBarState = LocalSnackbarState.current

    ElevatedCard(
        modifier = modifier,
        enabled = isReverbEnabled,
        onClick = {
            snackBarState.showInfoSnackbar(title = "This feature is coming soon")
        },
        content = {
            val maxWidth = Modifier.fillMaxWidth()
            Row(modifier = maxWidth, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(Res.strings.reverb))

                Switch(
                    checked = isReverbEnabled,
                    onCheckedChange = { isReverbEnabled = it }
                )
            }

            Slider(
                modifier= Modifier.padding(vertical = 8.dp),
                value = 0.5f,
                onValueChange = { }
            )
        }
    )
}