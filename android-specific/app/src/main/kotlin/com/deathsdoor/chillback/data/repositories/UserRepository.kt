package com.deathsdoor.chillback.data.repositories

import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.Callable

class UserRepository(private val musicRepository: MusicRepository) {
    val favouriteTracks by lazy { FavouriteTrackCollectionRepository(musicRepository) }
    val topPlayedTracks by lazy { TopPlayedTrackCollectionRepository() }
    val localTracks by lazy { LocalSongsRepository(musicRepository) }

    private fun launch(block: suspend CoroutineScope.() -> Unit) = musicRepository.coroutineScope.launch(block = block)
    private val database : ApplicationLocalDatabase get() = ApplicationLocalDatabase(musicRepository.context)

    fun createTrackCollection(track : TrackCollectionWithTracks) = launch {
        val db = database
        val collection = track.collection

        db.trackCollectionDao.insertOrUpdate(collection)

        val trackCrossReference = track.tracks
            .mapIndexed { index , it -> TrackCollectionCrossReference(collection_id = collection.id, track_id = it.id,index = index) }
            .toTypedArray()

        db.trackCollectionCrossReferenceDao.insertOrUpdateAll(*trackCrossReference)
    }

    fun removeTrackFromCollection(collection : TrackCollection,track : Track,index : Int) = launch {
        val reference = TrackCollectionCrossReference(collection,track,index)
        database.trackCollectionCrossReferenceDao.remove(reference)
    }

    fun changeFavouriteStatusForTrack(id : Long,status: Boolean) = launch {
        database.trackDao.updateFavouriteStatusFor(id,status)
    }

    fun deleteTrackCollection(trackCollection : TrackCollection) = launch {
        database.trackCollectionDao.removeTrackCollection(trackCollection.id)
    }

    fun rearrangeTracks(trackCollection: TrackCollection,rearrangedTracks : List<Track>) = launch {
        val references = rearrangedTracks.mapIndexed { index, track ->
            TrackCollectionCrossReference(trackCollection,track,index)
        }

        database.trackCollectionCrossReferenceDao.updateAll(references)
    }

    fun changeTrackCollectionPinStatus(collection : TrackCollection) = launch {
        collection.isPinned = !collection.isPinned
        database.trackCollectionDao.update(collection)
    }

    suspend fun tracksFor(collection : TrackCollection) = database.trackCollectionCrossReferenceDao.trackCountIn(collection.id)


    @Deprecated("Do not use this")
    fun removeTrack(track : Track) = launch {
        database.trackDao.remove(track.id)
    }

    private val _rawUserTrackCollections = MutableStateFlow<List<TrackCollection>?>(null)
    val userTrackCollections = _rawUserTrackCollections.onSubscription {
        if(_rawUserTrackCollections.value == null) initializeUserTrackCollections()
    }.stateIn(
        scope = musicRepository.coroutineScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = null
    )

    private fun initializeUserTrackCollections() = musicRepository.coroutineScope.launch {
        database.trackCollectionDao.trackCollections().distinctUntilChanged()
            .collectLatest { trackCollections ->
                val value = trackCollections.sortedBy { it.isPinned }

                _rawUserTrackCollections.emit(value)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun tracksForCollection(collection: TrackCollection): Flow<List<Track>> {
        return database.runInTransaction(Callable {
            database.trackCollectionCrossReferenceDao.trackCrossRefsFor(collection.id).mapLatest { references ->
                database.trackDao.tracksFrom(
                    references.sortedBy { it.index }.map { it.track_id }
                )
            }
        })
    }
}