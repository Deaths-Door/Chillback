package com.deathsdoor.chillback.core.media.repositories

import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroMediaMetadata
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.core.media.extensions.isOnDevice
import com.deathsdoor.chillback.core.media.extensions.toFile
import com.deathsdoor.chillback.core.media.resources.Res
import com.eygraber.uri.Uri
import io.github.reactivecircus.cache4k.Cache
import kotlinx.io.IOException
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.TagException
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.minutes

object MusixRepository {
    private val cachedMetadata = Cache.Builder<String, AstroMediaMetadata>()
        .maximumCacheSize(50)
        .expireAfterAccess(5.minutes)
        .build()

    /**
     * Retrieves the cached metadata for a given media item.
     *
     * This method is an alias for the `getValue` operator. It retrieves the cached metadata
     * for the provided media item using its ID.
     *
     * @param mediaItem The media item for which to retrieve metadata.
     * @return The cached `AstroMediaMetadata` for the media item, or null if not found.
     */
    fun metadataFromCache(mediaItem: AstroMediaItem): AstroMediaMetadata? = cachedMetadata.get(mediaItem.mediaId)

    /**
     * Attempts to retrieve the metadata for a media item.
     *
     * This method first checks the internal cache for the media item's ID.
     * If not found in the cache, it fetches the metadata
     *
     * It utilizes the `stackableSnackbarState` parameter (if provided) to report any errors encountered during the fetching process.
     * The retrieved metadata is then cached and returned.
     *
     * @param resource A LazyResourceLoader instance used to fetch the metadata if not found in cache.
     * @param mediaItem The media item for which to retrieve metadata.
     * @param stackableSnackbarState An optional parameter to report any errors to the user.
     * @return The `AstroMediaMetadata` object containing the retrieved data, or null if there's an error.
     */
    suspend fun metadataFromCacheOrFetch(
        resource : LazyResourceLoader,
        mediaItem: AstroMediaItem,
        stackableSnackbarState: StackableSnackbarState?
    ) = cachedMetadata.get(mediaItem.mediaId) ?: readMostImportantFields(resource, mediaItem, stackableSnackbarState)

    private fun audioFileFor(
        resource : LazyResourceLoader,
        mediaItem: AstroMediaItem,
        stackableSnackbarState: StackableSnackbarState?
    ): AudioFile? {
        require(mediaItem.isOnDevice()) { "Media item must be on device. This method can only be called for locally available media." }

        val file = mediaItem.source.toFile()

        return try { AudioFileIO.read(file) } catch (exception : Exception) {
            when(exception) {
                is CannotReadException, is IOException,
                is TagException, is ReadOnlyFileException,
                is InvalidAudioFrameException -> {
                    stackableSnackbarState?.showWarningSnackbar(
                        title = resource.stringResource(Res.strings.failed_fetching_media_info),
                        description = exception.localizedMessage
                    )

                    null
                }
                else -> throw exception
            }
        }
    }

    /**
     * Reads the most important metadata fields for a media item.
     *
     * This method retrieves the media item's tag using JAudioTagger and extracts the following
     * fields:
     *  - Title (displayTitle and title)
     *  - Artists (artist)
     *  - Album (albumTitle)
     *  - Artwork (artworkUri)
     *  - Genre (genre)
     *  - Duration in seconds (extras["duration"])
     * It then caches the retrieved metadata with an additional flag (`read_most_important`)
     * to indicate that these essential fields have been read.
     *
     * If an error occurs while reading the tag, it attempts to report the issue to the user
     * through the provided `stackableSnackbarState`.
     *
     * @param mediaItem The media item for which to read metadata.
     * @param stackableSnackbarState An optional parameter to report any errors to the user.
     * @return The `AstroMediaMetadata` object containing the read fields, or null if there's an error.
     */
    @Suppress("RedundantSuspendModifier")
    suspend fun readMostImportantFields(
        resource : LazyResourceLoader,
        mediaItem: AstroMediaItem,
        stackableSnackbarState: StackableSnackbarState?
    ) : AstroMediaMetadata? {
        val cachedMetadata = metadataFromCache(mediaItem)

        if(cachedMetadata?.extras?.get("read_most_important") != null) return cachedMetadata

        return audioFileFor(resource,mediaItem, stackableSnackbarState)?.let { audioFile ->
            val tag = audioFile.tagOrCreateDefault

            val displayTitle = tag.getFirst(FieldKey.TITLE)
            val artists = tag.getFirst(FieldKey.ARTISTS)
            val albumTitle = tag.getFirst(FieldKey.ALBUM)
            val artwork = tag.firstArtwork?.imageUrl?.let { Uri.parse(it) }

            val genre = tag.getFields(FieldKey.GENRE).joinToString(",")

            val extras = cachedMetadata?.extras?.toMutableMap() ?: mutableMapOf()
            extras["read_most_important"] = true

            extras.updateDuration(audioFile.audioHeader.trackLength)

            val updatedMetadata = cachedMetadata?.copy(
                displayTitle = displayTitle,
                title = displayTitle,
                artist = artists,
                albumTitle = albumTitle,
                artworkUri = artwork,
                genre = genre,
                extras = extras
            ) ?: AstroMediaMetadata(
                displayTitle = displayTitle,
                title = displayTitle,
                artist = artists,
                albumTitle = albumTitle,
                artworkUri = artwork,
                genre = genre,
                extras = extras
            )

            MusixRepository.cachedMetadata.put(mediaItem.mediaId,updatedMetadata)

            return updatedMetadata
        }
    }
}

