package com.deathsdoor.chillback.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_collections")
data class TrackCollection(
    val name : String,
    // TODO : CHECK IF THIS IS NEEDED
    val imageSource : Uri? = null,
    var isPinned : Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    var id : Long = 0

    override fun equals(other: Any?): Boolean {
        if (other !is TrackCollection || isPinned != other.isPinned) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (imageSource?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        return result
    }
}