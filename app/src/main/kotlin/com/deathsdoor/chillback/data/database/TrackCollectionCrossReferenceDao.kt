package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference

@Dao
sealed interface TrackCollectionCrossReferenceDao : CoreSharedDao<TrackCollectionCrossReference> {
    @Query("SELECT COUNT(track_id) FROM track_collection_tracks WHERE collection_id = :collectionId ")
    suspend fun trackCountIn(collectionId: Long) : Int

    @Deprecated("Remove the need for an index")
    @Delete
    suspend fun remove(reference: TrackCollectionCrossReference)
}