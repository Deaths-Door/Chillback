package com.deathsdoor.chillback.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "track_collection_tracks",
    primaryKeys = ["collection_id", "track_id"],
    foreignKeys = [
        ForeignKey(
            entity = TrackCollection::class,
            parentColumns = ["collection_id"],
            childColumns = ["collection_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Track::class,
            parentColumns = ["track_id"],
            childColumns = ["track_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TrackCollectionCrossReference constructor(
    val collection_id: Long,
    @ColumnInfo(index = true)
    val track_id: Long,
    // Index of track in the trackCollection.tracks list
    val index : Int
) {
    constructor(
        collection: TrackCollection,
        track : Track,
        index : Int
    ) : this(collection.id,track.id,index)
}