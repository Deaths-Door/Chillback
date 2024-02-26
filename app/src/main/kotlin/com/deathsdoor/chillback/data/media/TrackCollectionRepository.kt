package com.deathsdoor.chillback.data.media

import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface TrackCollectionRepository {
    val trackCollection : StateFlow<TrackCollectionWithTracks?>
    val isUserDefined : Boolean

    fun applyCoroutineScope(coroutineScope: CoroutineScope)
}