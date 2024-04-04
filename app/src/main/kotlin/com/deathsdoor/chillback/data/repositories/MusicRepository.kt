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

    fun updateDetailsCache(track: Track,details: TrackDetails) = tracksDetails.put(track.id,details)

    fun trackDetailsOrNull(track: Track) : TrackDetails? = tracksDetails.get(track.id)

    suspend fun trackDetails(track : Track): TrackDetails? {
        return trackDetailsOrNull(track = track) ?: run {
            val extractor = MediaMetadataExtractor(track) ?:  return@run null
            val name = extractor.name()
            val artwork = extractor.artwork()
            val artists = extractor.artists()
            val genre = extractor.genre()
            val album = extractor.album()
            val albumArtists = extractor.albumArtists()

            val details = TrackDetails(
                name = name,
                artwork = artwork,
                artists = artists.joinToString(","),
                genre = genre,
                album = album,
                albumArtists = albumArtists.joinToString(",")
            )

            tracksDetails.put(track.id,details)

            details
        }
    }

    suspend fun trackArtwork(track : Track) = tracksDetails.get(track.id)?.artwork ?: MediaMetadataExtractor(track)?.artwork()
    suspend fun trackGenre(track : Track) = tracksDetails.get(track.id)?.genre ?: MediaMetadataExtractor(track)?.genre()
    suspend fun trackAlbum(track : Track) = tracksDetails.get(track.id)?.album ?: MediaMetadataExtractor(track)?.album()
    suspend fun trackArtists(track : Track) = tracksDetails.get(track.id)?.artists ?: MediaMetadataExtractor(track)?.artists()?.joinToString(",")


}