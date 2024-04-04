package com.deathsdoor.chillback.data.repositories

import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import com.deathsdoor.chillback.data.models.TrackDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Should only be observed when showed , hence local coroutineScope
// But we need to cache this data , hence global
class FavouriteTrackCollectionRepository(private val musicRepository: MusicRepository) : TrackCollectionRepository {
    override val isUserDefined: Boolean = true

    private lateinit var mutableFlow : MutableStateFlow<TrackCollectionWithTracks?>

    // Empty Flow , should only work when applyCoroutineScope is called
    override var trackCollection: StateFlow<TrackCollectionWithTracks?> = mutableFlow.asStateFlow()
        private set

    override fun applyCoroutineScope(coroutineScope : CoroutineScope) {
        mutableFlow = MutableStateFlow(null)
        trackCollection = mutableFlow.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = null
        )

        coroutineScope.launch {
            val collection = TrackCollection(name = "Favourites")
            ApplicationLocalDatabase(musicRepository.context)
                .trackDao
                .favouriteTracks()
                .distinctUntilChanged()
                .collectLatest {
                    val value = TrackCollectionWithTracks(
                        collection = collection,
                        tracks = it
                    )

                    mutableFlow.emit(value)
                }
        }
    }
}

