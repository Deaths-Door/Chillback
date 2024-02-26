package com.deathsdoor.chillback.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.coroutines.flow.MutableStateFlow

data class TrackCollectionWithTracks(
    @Embedded val collection: TrackCollection,
    @Relation(
        parentColumn = "collection_id",
        entityColumn = "track_id",
        associateBy = Junction(TrackCollectionCrossReference::class)
    )
    val tracks: List<Track> = emptyList()
)