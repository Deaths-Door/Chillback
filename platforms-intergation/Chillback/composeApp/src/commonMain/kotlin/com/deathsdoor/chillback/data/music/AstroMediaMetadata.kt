package com.deathsdoor.chillback.data.music

import com.eygraber.uri.Uri
import java.io.Serializable

data class AstroMediaMetadata(
     val title: CharSequence? = null,
     val artist: CharSequence? = null,
     val albumTitle: CharSequence? = null,
     val albumArtist: CharSequence? = null,
     val displayTitle: CharSequence? = null,
     val subtitle: CharSequence? = null,
     val description: CharSequence? = null,
     val artworkUri: Uri? = null,
     val trackNumber: Int? = null,
     val totalTrackCount: Int? = null,
     val recordingYear: Int? = null,
     val recordingMonth: Int? = null,
     val recordingDay: Int? = null,
     val releaseYear: Int? = null,
     val releaseMonth: Int? = null,
     val releaseDay: Int? = null,
     val writer: CharSequence? = null,
     val composer: CharSequence? = null,
     val conductor: CharSequence? = null,
     val discNumber: Int? = null,
     val totalDiscCount: Int? = null,
     val genre: CharSequence? = null,
     val compilation: CharSequence? = null,
     val station: CharSequence? = null,
     val extras: Serializable? = null
)