package com.deathsdoor.chillback.data.backgroundtasks

import com.deathsdoor.chillback.data.database.ApplicationDatabase
import com.deathsdoor.chillback.data.music.MediaPlaybackPreferences
import com.deathsdoor.chillback.data.preferences.ApplicationPreferences
import com.deathsdoor.chillback.data.preferences.playback
import com.deathsdoor.chillback.ui.state.ChillbackApplicationState

suspend fun callImmediatelyOnApplicationFinish(applicationState: ChillbackApplicationState){
    if(ApplicationDatabase.showDataLocally && applicationState.astroPlayer != null)
            applicationState.preferences.playback.update(MediaPlaybackPreferences.from(applicationState.astroPlayer!!))

}
