package com.deathsdoor.chillback.data.extensions

import com.deathsdoor.chillback.data.database.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.music.AstroMediaItem
import com.deathsdoor.chillback.data.music.AstroMediaMetadata
import com.deathsdoor.chillback.data.repositories.MusicRepository


suspend fun Track.asAstroMediaItem(musicRepository: MusicRepository): AstroMediaItem {
    val details = musicRepository.trackDetails(this)
    return AstroMediaItem(
        mediaId = this.id.toString(),
        source = this.source,
        metadata = details?.asAstroMediaMetadata()
    )
}