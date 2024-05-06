package com.deathsdoor.chillback.data.music

import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.deathsdoor.chillback.data.music.RepeatMode.*

internal fun RepeatMode.toPlayerRepeatMode() : @Player.RepeatMode Int = when(this) {
    Off -> REPEAT_MODE_OFF
    All -> REPEAT_MODE_ONE
    One -> REPEAT_MODE_ALL
}

internal fun (@Player.RepeatMode Int).toRepeatMode() = when(this) {
    REPEAT_MODE_OFF -> Off
    REPEAT_MODE_ONE -> One
    REPEAT_MODE_ALL -> All
    else -> error("Unreachable")
}