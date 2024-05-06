package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Query
import com.deathsdoor.chillback.data.models.Track
import kotlinx.coroutines.flow.Flow

@Dao
sealed interface TrackDao : CoreSharedDao<Track> {
    @Query("SELECT * FROM tracks WHERE track_id in (:ids)")
    suspend fun tracksFrom(ids : List<Long>) : List<Track>

    @Query("SELECT * FROM tracks WHERE is_favourite == 1")
    fun favouriteTracks() : Flow<List<Track>>

    @Query("UPDATE tracks SET is_favourite = :status WHERE track_id == :id ")
    suspend fun updateFavouriteStatusFor(id : Long,status : Boolean)

    @Query("SELECT * FROM tracks")
    /// Get all tracks as source is only local rn so it works
    suspend fun tracks() : List<Track>

    @Query("DELETE FROM tracks WHERE track_id = :trackId")
    suspend fun remove(trackId : Long)
}