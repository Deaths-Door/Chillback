package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Query
import com.deathsdoor.chillback.data.models.TrackCollection
import kotlinx.coroutines.flow.Flow

@Dao
sealed interface TrackCollectionDao : CoreSharedDao<TrackCollection> {
    @Query("SELECT * FROM track_collections")
    fun trackCollections() : Flow<List<TrackCollection>>

    @Query("DELETE FROM track_collections WHERE collection_id = :collectionId")
    suspend fun removeTrackCollection(collectionId : Long)
}