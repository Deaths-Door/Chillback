package com.deathsdoor.chillback.data.viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.mediaplayer.MediaPlaybackPreferences
import com.deathsdoor.chillback.data.mediaplayer.MediaPlaybackService
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.providers.LocalSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoreViewModel constructor(
    context: Context,
) : ViewModel()  {
    val settings: Settings = Settings(context)
    var mediaController: MediaController? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaPlaybackService = MediaPlaybackService()

            mediaController = mediaPlaybackService.createMediaController(context).get().apply mediaController@ {
                val preferences = settings.mediaPlaybackPreferences.first()

                // Needs to be called from the main thread
                withContext(Dispatchers.Main) {
                    preferences.applyPreferencesTo(this@mediaController)
                }
            }
        }
    }

    override fun onCleared() {
        mediaController?.let {
            viewModelScope.launch {
                // We get preferences / 'state' of the mediaController at end and save it , and restore it for later
                val preferences = MediaPlaybackPreferences.from(it)
                settings.updateMediaPlaybackPreferences(preferences)
            }
        }
        super.onCleared()
    }
}