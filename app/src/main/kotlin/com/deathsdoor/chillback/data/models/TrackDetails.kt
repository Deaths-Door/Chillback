package com.deathsdoor.chillback.data.models

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.deathsdoor.chillback.data.extensions.setIsFavourite

data class TrackDetails (
    val name : String,
    val artwork : Uri?,
    val artists : String?,
    val genre : String,
    val album : String,
    val albumArtists : String?,
) {
    fun asMediaItem(track: Track) = MediaItem.Builder()
        .setMediaId(track.id.toString())
        .setUri(track.sourcePath)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(name)
                .setArtworkUri(artwork)
                .setGenre(genre)
                .setAlbumTitle(album)
                .setAlbumArtist(albumArtists)
                .setArtist(artists)
                .setIsFavourite(track)
                .build()
        )
        .build()

    fun artistWithOthers() = artists?.let { artist ->
        val count = artist.count { it == ',' }

        if (count == 0) artist
        else "${artist.subSequence(0,artist.indexOfFirst { it == ',' })} and $count others"
    }

    companion object {
        fun from(mediaItem: MediaItem) = with(mediaItem.mediaMetadata) {
            TrackDetails(
                name = title.toString(),
                artwork = artworkUri,
                genre = genre.toString(),
                album = albumTitle.toString(),
                albumArtists = albumArtist.toString(),
                artists = artist.toString()
            )
        }
    }
}
