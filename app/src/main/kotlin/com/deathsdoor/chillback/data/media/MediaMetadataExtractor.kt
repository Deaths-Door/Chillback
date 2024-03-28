package com.deathsdoor.chillback.data.media

import android.net.Uri
import com.deathsdoor.chillback.data.models.Track
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

@Suppress("RedundantSuspendModifier")
@JvmInline
value class MediaMetadataExtractor private constructor(private val audioFile: AudioFile) {
    companion object {
        // TODO : DON"T CATCH ALL EXCEPTIONS MAYBE
        operator fun invoke(track : Track): MediaMetadataExtractor? {
            return try {
                val file = File(track.sourcePath)
                val audioFile = AudioFileIO.read(file)
                MediaMetadataExtractor(audioFile)
            }
            catch(exception : Exception) {
                null
            }
        }

        fun audioFileFrom(source : String): AudioFile = AudioFileIO.read(File(source))
    }

    private val tag get() = audioFile.tagOrCreateDefault

    suspend fun name(): String = tag.getFirst(FieldKey.TITLE)
    suspend fun artwork(): Uri? = tag.firstArtwork?.imageUrl?.let {  Uri.parse(it) }
    suspend fun album(): String = tag.getFirst(FieldKey.ALBUM)
    suspend fun genre() : String = tag.getFirst(FieldKey.GENRE)
    suspend fun artists() : List<String> = tag.getAll(FieldKey.ARTIST)
    suspend fun albumArtists() : List<String> = tag.getAll(FieldKey.ALBUM_ARTISTS)
}