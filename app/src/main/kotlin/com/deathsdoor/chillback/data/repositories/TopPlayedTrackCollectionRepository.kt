package com.deathsdoor.chillback.data.repositories

import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

// TODO : Implement this class , when i implement data collection for tracks and media
class TopPlayedTrackCollectionRepository : TrackCollectionRepository {
    override val trackCollection: StateFlow<TrackCollectionWithTracks?> = throw NotImplementedError()
    override val isUserDefined: Boolean = true

    override fun applyCoroutineScope(coroutineScope: CoroutineScope) = throw NotImplementedError()
}
