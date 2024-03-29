package com.deathsdoor.chillback.data.repositories

import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserTrackCollectionRepository(
    val userRepository: UserRepository,
    val musicRepository : MusicRepository,
    val collection : TrackCollection
) : TrackCollectionRepository {
    private lateinit var mutableFlow : MutableStateFlow<TrackCollectionWithTracks?>
    override var trackCollection: StateFlow<TrackCollectionWithTracks?> = mutableFlow.asStateFlow()
        private set
    override val isUserDefined: Boolean = false

    override fun applyCoroutineScope(coroutineScope: CoroutineScope) {
        mutableFlow = MutableStateFlow(null)
        trackCollection = mutableFlow.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = null
        )

        coroutineScope.launch {
            userRepository.tracksForCollection(collection).collectLatest {
                mutableFlow.emit(
                    TrackCollectionWithTracks(
                        collection = collection,
                        tracks = it
                    )
                )
            }
        }
    }
}