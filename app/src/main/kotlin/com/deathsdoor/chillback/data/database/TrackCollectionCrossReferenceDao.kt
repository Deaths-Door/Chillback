package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Delete
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference

@Dao
sealed interface TrackCollectionCrossReferenceDao : CoreSharedDao<TrackCollectionCrossReference> {
    @Delete
    suspend fun remove(reference: TrackCollectionCrossReference)
}