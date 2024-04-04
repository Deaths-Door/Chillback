package com.deathsdoor.chillback.data.repositories

import android.provider.MediaStore
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.SupportedFileFormat
import java.util.Locale
import kotlin.time.Duration.Companion.minutes

class LocalSongsRepository(val musicRepository: MusicRepository) {
    private val _tracks: MutableStateFlow<List<Track>?> = MutableStateFlow(null)
    var tracks = _tracks.asStateFlow()
    //  val tracks : List<Track> get() = _tracks

    private val groupByCache = Cache.Builder<Int, Map<String, List<Track>>>()
        .maximumCacheSize(3) // Ensure this value is always Self.items.size - 1
        .expireAfterAccess(2.minutes)
        .build()

    suspend fun sorted(currentItem: Int): List<TrackCollection> = groupByCache.get(currentItem) {
        _tracks.value!!.groupBy {
            when (currentItem) {
                1 -> musicRepository.trackGenre(it) ?: ""
                2 -> musicRepository.trackAlbum(it) ?: ""
                3 -> musicRepository.trackArtists(it) ?: ""
                else -> throw IllegalStateException("Should be unreachable")
            }
        }
        // TODO: Filter any empty artists fieilds??
    }.map { entry -> TrackCollection(entry.key) }

    companion object {
        // TODO : CHANGE ICONS
        val items by lazy {
            mapOf(
                "Songs" to R.drawable.add_to_queue,
                "Genre" to R.drawable.add_to_queue,
                "Album" to R.drawable.add_to_queue,
                "Artists" to R.drawable.add_to_queue,
            )
        }
    }

    fun applyCoroutineScope(coroutineScope: CoroutineScope) {
        tracks = _tracks.stateIn(
            scope = coroutineScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5000L)
        )

        coroutineScope.launch(Dispatchers.IO) {
            val tracks = extractExternalMusicFiles()
            _tracks.emit(tracks)

            ApplicationLocalDatabase(musicRepository.context)
                .trackDao
                .insertOrUpdateAll(*tracks.toTypedArray())
        }
    }

    private suspend fun extractExternalMusicFiles(): MutableList<Track> {
        val database = ApplicationLocalDatabase(musicRepository.context)
        val indexedTrackMap = database.trackDao.tracks().associateBy { it.sourcePath }

        val discoveredTracks = mutableListOf<Track>()
        val tracks = mutableListOf<Track>()

        musicRepository.context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media.DATA),
            MediaStore.Audio.Media.IS_MUSIC + "!= 0",
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val column = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val source = cursor.getString(column)

                val indexedItem = indexedTrackMap[source]
                if (indexedItem != null) {
                    tracks.add(indexedItem)
                    continue
                }

                // Same as  File(..).extension
                val extension = source.substringAfterLast('.', "").uppercase(Locale.getDefault())

                try {
                    // Check if it is a supported type then only add it
                    SupportedFileFormat.valueOf(extension)

                    val track = Track(source)
                    discoveredTracks.add(track)
                    tracks.add(track)
                } catch (exception: IllegalArgumentException) {
                    // TODO : Inform user about skipped files??
                    // Exception in thread "main" java.lang.IllegalArgumentException: No enum constant SupportedFileFormat.$extension
                }
            }
        }

        database.trackDao.insertOrIgnoreAll(*discoveredTracks.toTypedArray())
        return tracks
    }
}