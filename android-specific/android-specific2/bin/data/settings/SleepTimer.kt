package com.deathsdoor.chillback.data.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsCheckbox
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
sealed class SleepTimer private constructor(){
    @Serializable
    object Disabled : SleepTimer()

    @Serializable
    object AfterCurrentPlaylist : SleepTimer()

    @Serializable
    class AfterNSongs(val number : Int = 1) : SleepTimer()

    @Serializable
    class AfterTime(val time: Duration = 15.minutes) : SleepTimer()

    override fun toString(): String = Json.encodeToString(this)

    fun start() {
        if(this is Disabled) return

        // TODO(Implement this)
    }
}