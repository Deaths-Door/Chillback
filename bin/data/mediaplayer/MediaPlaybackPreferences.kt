package com.deathsdoor.chillback.data.mediaplayer

import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.session.MediaController
import kotlinx.serialization.Serializable

// TODO : Save other things like playback queue ect
@Serializable
class MediaPlaybackPreferences private constructor(
    private val shuffleModeEnabled : Boolean,
    private val repeatMode : @Player.RepeatMode Int,
) {
    constructor() : this(false,REPEAT_MODE_OFF)

    companion object {
        fun from(mediaController: MediaController) = MediaPlaybackPreferences(
            shuffleModeEnabled = mediaController.shuffleModeEnabled,
            repeatMode = mediaController.repeatMode
        )
    }

    fun applyPreferencesTo(mediaController: MediaController): MediaController = mediaController.apply {
        shuffleModeEnabled = this@MediaPlaybackPreferences.shuffleModeEnabled
        repeatMode = this@MediaPlaybackPreferences.repeatMode
    }
}
