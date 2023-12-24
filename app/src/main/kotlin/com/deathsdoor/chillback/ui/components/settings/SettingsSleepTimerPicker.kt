package com.deathsdoor.chillback.ui.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.alorma.compose.settings.ui.SettingsSlider
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.data.settings.SleepTimer
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.time.Duration

@Composable
fun SettingsSleepTimerPicker(settings: Settings) {
    val coroutineScope = rememberCoroutineScope()

    val sleepTimer by settings.sleepTimer.collectAsState(SleepTimer.Disabled)
    val state = rememberBooleanSettingState(sleepTimer !is SleepTimer.Disabled)

    var dialogState by remember { mutableStateOf(true) }

    SettingsCheckbox(
        state = state,
        title = { Text("Set the sleep timer") },
        subtitle = { Text("Set a timer to automatically stop playback after a specified time.") },
        onCheckedChange = {
            dialogState = it

            if(it) return@SettingsCheckbox

            coroutineScope.launch {
                // Changed to false so disable it
                if(!it) settings.updateSleepTimer(SleepTimer.Disabled)
            }
        }
    )

    if(dialogState) return

    var selectedOption by remember(sleepTimer) { mutableStateOf(sleepTimer) }

    AlertDialog(
        onDismissRequest = { dialogState = false },
        title = { Text(text = "Choose your option") },
        text = {
            SleepTimer.AfterCurrentPlaylist.Content(
                sleepTimer = sleepTimer,
                enabled = selectedOption is SleepTimer.AfterCurrentPlaylist,
                onChange = { selectedOption = SleepTimer.AfterCurrentPlaylist }
            )

            SleepTimer.AfterNSongs.Content(
                sleepTimer = sleepTimer,
                enabled = selectedOption is SleepTimer.AfterNSongs,
                onChange = { selectedOption = SleepTimer.AfterNSongs(it) }
            )

            SleepTimer.AfterTime.Content(
                sleepTimer = sleepTimer,
                enabled = selectedOption is SleepTimer.AfterTime,
                onChange = { selectedOption = SleepTimer.AfterTime(it) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { coroutineScope.launch { settings.updateSleepTimer(selectedOption) } },
                content = { Text("Confirm") }
            )
        },
    )
}


@Composable
private fun SleepTimer.AfterCurrentPlaylist.Content(
    enabled : Boolean,
    sleepTimer: SleepTimer,
    onChange : () -> Unit
) = SettingsCheckbox(
    state = rememberBooleanSettingState(sleepTimer is SleepTimer.AfterCurrentPlaylist),
    enabled = enabled,
    title = { Text("Sleep after current playlist") },
    onCheckedChange = {
        if(it) onChange()
    }
)

@Composable
private fun SleepTimer.AfterNSongs.Companion.Content(
    enabled : Boolean,
    sleepTimer: SleepTimer,
    onChange : (Int) -> Unit
) {
    val sliderState = rememberIntSettingState(
        if(sleepTimer is SleepTimer.AfterNSongs) sleepTimer.number
        else 5
    )

    SettingsSlider(
        state = sliderState,
        enabled = enabled,
        valueRange = 1f..15f,
        onValueChangeFinished = { onChange(sliderState.value) },
        title = { Text("Sleep after ${sliderState.value} songs") }
    )
}

@Composable
private fun SleepTimer.AfterTime.Companion.Content(
    enabled : Boolean,
    sleepTimer: SleepTimer,
    onChange : (snappedTime: Duration) -> Unit
){
    val state = rememberBooleanSettingState(sleepTimer is SleepTimer.AfterTime)

    SettingsCheckbox(
        state = state,
        enabled = enabled,
        title = { Text("Sleep ${if (sleepTimer is SleepTimer.AfterTime) "in ${sleepTimer.time}" else "after sometime"}") },
    )

    AnimatedVisibility(visible = state.value) {
        WheelTimePicker(
            startTime = LocalTime.of(0,15),
            minTime = LocalTime.of(0,0,),
            maxTime = LocalTime.of(2,59),
            onSnappedTime = {
                val duration = Duration.parse(it.toString())
                onChange(duration)
            }
        )
    }
}