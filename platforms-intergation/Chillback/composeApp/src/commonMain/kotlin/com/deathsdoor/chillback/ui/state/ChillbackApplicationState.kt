package com.deathsdoor.chillback.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deathsdoor.chillback.components.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.data.music.AstroPlayer
import com.deathsdoor.chillback.data.preferences.ApplicationPreferences
import com.deathsdoor.chillback.data.preferences.current
import com.deathsdoor.chillback.data.preferences.playback
import com.deathsdoor.chillback.data.repositories.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChillbackApplicationState(
    val preferences : ApplicationPreferences,
    stackedSnackbarHostState : StackableSnackbarState
) : ViewModel() {
    private val musicRepository = MusicRepository(coroutineScope = viewModelScope,)
    var astroPlayer : AstroPlayer? = null

    init {
        viewModelScope.launch {
            astroPlayer = createAstroPlayer()

            // TODO : Update to firebase database somehow
            preferences.playback.current().first()?.apply(
                astroPlayer = astroPlayer!!,
                stackedSnackbarHostState = stackedSnackbarHostState,
                musicRepository = musicRepository
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        this@ChillbackApplicationState.onKmpCleared()
    }
}