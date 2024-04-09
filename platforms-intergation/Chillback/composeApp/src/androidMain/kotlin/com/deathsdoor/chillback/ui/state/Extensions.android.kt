package com.deathsdoor.chillback.ui.state

import com.deathsdoor.chillback.data.backgroundtasks.MediaPlaybackPreferenceWorkManager
import com.deathsdoor.chillback.data.database.ApplicationDatabase
import com.deathsdoor.chillback.data.extensions.applicationContext
import com.deathsdoor.chillback.data.music.AstroPlayer
import com.deathsdoor.chillback.data.music.MediaPlaybackService

actual suspend fun createAstroPlayer(): AstroPlayer {
    val future = AstroPlayer.createFrom(MediaPlaybackService())
    return AstroPlayer.createFrom(future)
}

actual fun ChillbackApplicationState.onKmpCleared() {
    if(ApplicationDatabase.showDataLocally && astroPlayer != null)
        MediaPlaybackPreferenceWorkManager.start(applicationContext,astroPlayer!!)
}