package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference

@Dao
sealed interface TrackCollectionCrossReferenceDao : CoreSharedDao<TrackCollectionCrossReference> {
    @Delete
    suspend fun remove(reference: TrackCollectionCrossReference)
}