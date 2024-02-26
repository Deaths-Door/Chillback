package com.deathsdoor.chillback.data.repositories

import android.content.Context
import com.deathsdoor.chillback.data.media.MediaMetadataExtractor
import com.deathsdoor.chillback.data.media.PlaybackQueueAsFlow
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration.Companion.minutes

class MusicRepository(
    internal val coroutineScope : CoroutineScope,
    internal val context : Context
) {
    val playbackQueue by lazy { PlaybackQueueAsFlow() }

    private val tracksDetails = Cache.Builder<Long, TrackDetails>()
        .maximumCacheSize(50)
        .expireAfterAccess(5.minutes)
        .build()

    // TODO : Figure out a better solution then this , temporary fix
    suspend fun trackDetails(track : Track): TrackDetails = tracksDetails.get(track.id) {
        val extractor = MediaMetadataExtractor(track) //?: return@apply null
        TrackDetails(
            name = extractor?.name() ?: "Failed reading data",
            artwork = extractor?.artwork(),
            artists = extractor?.artists()?.joinToString(","),
            genre = extractor?.genre() ?: "Failed reading data",
            album = extractor?.album() ?: "Failed reading data",
            albumArtists = extractor?.albumArtists()?.joinToString(",")
        )
    }

    suspend fun trackArtwork(track : Track) = tracksDetails.get(track.id)?.artwork ?: MediaMetadataExtractor(track)?.artwork()
}