package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
sealed interface TrackCollectionDao : CoreSharedDao<TrackCollection> {
    @Query("SELECT * FROM track_collections")
    fun trackCollections() : Flow<List<TrackCollection>>

    @Query("DELETE FROM track_collections WHERE collection_id = :collectionId")
    suspend fun _removeTrackCollection(collectionId : Long)

    suspend fun removeTrackCollection(trackCollection : TrackCollection) = _removeTrackCollection(trackCollection.id)
}