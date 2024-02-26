package com.deathsdoor.chillback.ui.state

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.navigation.NavHostController
import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.media.MediaPlaybackPreferences
import com.deathsdoor.chillback.data.media.MediaPlaybackService
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.preferences.ApplicationSettings
import com.deathsdoor.chillback.data.preferences.ApplicationSettings.Settings.Companion.update
import com.deathsdoor.chillback.data.repositories.MusicRepository
import com.deathsdoor.chillback.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChillbackAppState constructor(application : Application,val navController: NavHostController) : AndroidViewModel(application) {
    private val context: Context get() = this.getApplication<Application>().applicationContext

    val settings : ApplicationSettings = ApplicationSettings(context)

    val musicRepository = MusicRepository(
        coroutineScope = viewModelScope,
        context = context
    )

    val userRepository = UserRepository(musicRepository = musicRepository)

    var mediaController: MediaController? = null
        private set

    var trackCollectionRepository : TrackCollectionRepository? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaPlaybackService = MediaPlaybackService()

            mediaController = mediaPlaybackService.createMediaController(context).get()

            val database = ApplicationLocalDatabase(context)

            // Needs to be called from the main thread
            withContext(Dispatchers.Main) {
                val preferences = settings.music.playback(mediaController!!).current().first()
                preferences.apply(database,musicRepository,mediaController!!)
            }
        }
    }

    override fun onCleared() {
        mediaController?.let { viewModelScope.update(settings.music.playback(it),MediaPlaybackPreferences.from(it)); }
        super.onCleared()
    }
}