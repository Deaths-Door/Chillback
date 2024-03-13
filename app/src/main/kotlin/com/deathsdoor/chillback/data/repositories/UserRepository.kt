package com.deathsdoor.chillback.data.repositories

import com.deathsdoor.chillback.data.database.ApplicationLocalDatabase
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    fun deleteTrackCollection(track : TrackCollection) = launch {
        database.trackCollectionDao.removeTrackCollection(track)
    }

    // TODO : CUSTOM GET USER TRACK COLLECTION -> ORDER TRACKS CROSS REF BY index
    fun rearrangeTracks(trackCollection: TrackCollection,rearrangedTracks : List<Track>) = launch {
        val references = rearrangedTracks.mapIndexed { index, track ->
            TrackCollectionCrossReference(trackCollection,track,index)
        }

        database.trackCollectionCrossReferenceDao.updateAll(references)
    }

    fun changeTrackCollectionPinStatus(track : TrackCollection) = launch {
        track.isPinned = !track.isPinned
        database.trackCollectionDao.update(track)
    }

    fun removeTrack(track : Track) = launch {
        database.trackDao.remove(track.id)
    }
    /*



    private val _userTrackCollections = MutableStateFlow<List<TrackCollectionWithTracks>?>(null)
    val userTrackCollections = _userTrackCollections.onSubscription {
            if(_userTrackCollections.value == null) initializeUserTrackCollections()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = null
        )

    private fun initializeUserTrackCollections() = viewModelScope.launch {
        ApplicationLocalDatabase(context)
            .trackCollectionDao
            .trackCollections()
            .distinctUntilChanged()
            .collect { trackCollections ->
                val value = trackCollections.map { TrackCollectionWithTracks(collection = it) }
                _userTrackCollections.emit(value)
            }
    }*/
}