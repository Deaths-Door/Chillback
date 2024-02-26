package com.deathsdoor.chillback.data.repositories

import android.provider.MediaStore
import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.SupportedFileFormat
import java.io.File
import java.util.Locale

class LocalSongsRepository(val musicRepository: MusicRepository) {
    // TODO : USE SAME FOR All [TrackRepository] Implementations
    private val mutableFlow : MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())
    var tracks : StateFlow<List<Track>> = mutableFlow.asStateFlow()
        private set

    fun applyCoroutineScope(coroutineScope: CoroutineScope) {
        tracks = mutableFlow.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = emptyList()
        )

        coroutineScope.launch(Dispatchers.IO) {
            val tracks = extractExternalMusicFiles()
            mutableFlow.emit(tracks)
        }
    }

    private suspend fun extractExternalMusicFiles() : List<Track> {
        val database = ApplicationLocalDatabase(musicRepository.context)
        val indexedTrackMap = database.trackDao.tracks().associateBy { it.sourcePath }

        val tracks = mutableListOf<Track>()
        val discoveredTracks = mutableListOf<Track>()

        musicRepository.context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media.DATA),
            MediaStore.Audio.Media.IS_MUSIC + "!= 0",
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val column =  cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val source = cursor.getString(column)

                val indexedItem = indexedTrackMap[source]
                if(indexedItem != null) {
                    tracks.add(indexedItem)
                    continue
                }

                // Same as  File(..).extension
                val extension= source.substringAfterLast('.', "").uppercase(Locale.getDefault())

                try {
                    // Check if it is a supported type then only add it
                    SupportedFileFormat.valueOf(extension)

                    val track = Track(source)
                    discoveredTracks.add(track)
                    tracks.add(track)
                } catch (exception : IllegalArgumentException) {
                    // TODO : Inform user about skipped files??
                    // Exception in thread "main" java.lang.IllegalArgumentException: No enum constant SupportedFileFormat.$extension
                }
            }
        }

        database.trackDao.insertOrIgnoreAll(*discoveredTracks.toTypedArray())

        return tracks
    }
}