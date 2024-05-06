package com.deathsdoor.chillback.data.models

import com.deathsdoor.chillback.data.music.AstroMediaMetadata
import com.eygraber.uri.Uri


/// We are using this for two main reasons
/// a) To keep the AstroPlayer and my code seperate
/// b) IMPORTANT as most of the fields are going to be used in my application most the time
data class TrackDetails (
    val name : String,
    val artwork : Uri?,
    val artists : String?,
    val genre : String,
    val album : String,
    val albumArtists : String?,
) {

    // TODO : maybe read all the fields??
    fun asAstroMediaMetadata() = AstroMediaMetadata(
        title = name,
        artworkUri = artwork,
        artist = artists,
        genre = genre,
        albumTitle = album,
        albumArtist =albumArtists
    )
}