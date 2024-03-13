package com.deathsdoor.chillback.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deathsdoor.chillback.data.repositories.MusicRepository
import java.net.URLDecoder
import java.net.URLEncoder

@Entity(tableName = "tracks")
data class Track(@ColumnInfo(name = "source_path") val sourcePath : String) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "track_id")
    var id  : Long = 0
    @ColumnInfo(name = "is_favourite")
    var isFavorite : Boolean = false

    suspend fun asMediaItem(musicRepository : MusicRepository) = musicRepository.trackDetails(this).asMediaItem(this)

    fun encodeSourcePath() = URLEncoder.encode(sourcePath,"UTF-8")

    companion object {
        fun decodeSourcePath(sourcePath : String) = URLDecoder.decode(sourcePath,"UTF-8")
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Track || isFavorite != other.isFavorite) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + sourcePath.hashCode()
        return result
    }
}