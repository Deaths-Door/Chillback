package com.deathsdoor.chillback.ui.state

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.MetadataCategory
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.models.TrackMetadata
import com.deathsdoor.chillback.data.models.TrackMetadataInputType
import com.deathsdoor.chillback.data.models.TrackMetadataInputType.CheckBoxWithNoRecommendation
import com.deathsdoor.chillback.data.models.TrackMetadataInputType.DateWithNoRecommendation
import com.deathsdoor.chillback.data.models.TrackMetadataInputType.IntegerWithNoRecommendation
import com.deathsdoor.chillback.data.models.TrackMetadataInputType.TextWithNoRecommendation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag

@Stable
class MetadataEditScreenState : ViewModel() {
    // So only the ones that are 'loaded' into the screen are updated
    val metadataFields = listOf(
        lazy { TrackMetadata.SingleValue(FieldKey.KEY, 220u, R.string.field_key_key , MetadataCategory.AUDIO_PROPERTIES,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.TIMBRE, 210u, R.string.field_key_timbre , MetadataCategory.AUDIO_PROPERTIES,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.GENRE, 200u, R.string.field_key_genre , MetadataCategory.AUDIO_PROPERTIES,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ALBUM_SORT, 190u, R.string.field_key_album_sort , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ALBUM, 180u, R.string.field_key_album , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ALBUM_ARTIST, 170u, R.string.field_key_album_artist , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ALBUM_ARTIST_SORT, 160u, R.string.field_key_album_artist_sort , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ISRC, 150u, R.string.field_key_isrc , MetadataCategory.AUDIO_FINGERPRINT,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ORIGINAL_ARTIST, 150u, R.string.field_key_original_artist , MetadataCategory.ARTIST,
            TextWithNoRecommendation
        ) }, // Assuming original artist is also a single value
        lazy { TrackMetadata.MultipleValues(FieldKey.TAGS, 150u, R.string.field_key_tags , MetadataCategory.ADDITIONAL_CONTENT,
            TextWithNoRecommendation
        ) },  // Tags can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.COPYRIGHT, 150u, R.string.field_key_copyright , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_ARTISTID, 150u, R.string.field_key_musicbrainz_artistid , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.ALBUM_YEAR, 140u, R.string.field_key_album_year , MetadataCategory.ALBUM,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.COUNTRY, 140u, R.string.field_key_country , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_DISC_ID, 140u, R.string.field_key_musicbrainz_disc_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.ARTIST, 130u, R.string.field_key_artist , MetadataCategory.ARTIST,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.ARTISTS, 125u, R.string.field_key_artists , MetadataCategory.ARTIST,
            TextWithNoRecommendation
        ) }, // Artists can have multiple values
        lazy { TrackMetadata.MultipleValues(FieldKey.GROUP, 130u, R.string.field_key_group , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Groups can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, 130u, R.string.field_key_musicbrainz_original_release_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.ALBUM_ARTISTS, 120u, R.string.field_key_album_artists , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },  // Album artists can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.IS_CLASSICAL, 120u,  R.string.field_key_is_classical , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.INSTRUMENT, 120u, R.string.field_key_instrument , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Instruments can have multiple values
        lazy { TrackMetadata.MultipleValues(FieldKey.PERFORMER, 120u, R.string.field_key_performer , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },  // Performers can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RECORDING_WORK, 120u, R.string.field_key_musicbrainz_recording_work , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.ALBUM_ARTISTS_SORT, 110u, R.string.field_key_album_artists_sort , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },  // Album artist sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.IS_COMPILATION, 110u,  R.string.field_key_is_compilation , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.LANGUAGE, 110u, R.string.field_key_language , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.PERFORMER_NAME, 110u, R.string.field_key_performer_name , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },  // Performer names can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RECORDING_WORK_ID, 110u, R.string.field_key_musicbrainz_recording_work_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(fieldKey = FieldKey.BARCODE, frequencyOfUsage = 100u, stringId = R.string.field_key_barcode, category = MetadataCategory.ADDITIONAL_CONTENT , inputType = TextWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.ACOUSTID_FINGERPRINT, 100u, R.string.field_key_acoustid_fingerprint , MetadataCategory.AUDIO_FINGERPRINT,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.COMMENT, 100u, R.string.field_key_comment , MetadataCategory.ADDITIONAL_CONTENT,
            TextWithNoRecommendation
        ) },  // Comments can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.URL_DISCOGS_ARTIST_SITE, 100u, R.string.field_key_url_discogs_artist_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.discogs) },
        lazy { TrackMetadata.SingleValue(FieldKey.IS_GREATEST_HITS, 100u,  R.string.field_key_is_greatest_hits , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.OCCASION, 100u, R.string.field_key_occasion , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.PERFORMER_NAME_SORT, 100u, R.string.field_key_performer_name_sort , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Performer name sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASEARTISTID, 100u, R.string.field_key_musicbrainz_releaseartistid , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.CATALOG_NO, 90u, R.string.field_key_catalog_no , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_DISCOGS_RELEASE_SITE, 90u, R.string.field_key_url_discogs_release_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.discogs) },
        lazy { TrackMetadata.SingleValue(FieldKey.IS_HD, 90u,  R.string.field_key_is_hd , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.MOOD, 90u,  R.string.field_key_mood , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Moods can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.OPUS, 90u, R.string.field_key_opus , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.PRODUCER, 90u, R.string.field_key_producer , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Producers can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASEID, 90u, R.string.field_key_musicbrainz_releaseid , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy {TrackMetadata.SingleValue(fieldKey = FieldKey.SONGKONG_ID, frequencyOfUsage = 80u, stringId = R.string.field_key_songkick_id, category = MetadataCategory.ADDITIONAL_CONTENT, inputType = TextWithNoRecommendation)  },
        lazy { TrackMetadata.SingleValue(FieldKey.ACOUSTID_ID, 80u, R.string.field_key_acoustid_id , MetadataCategory.AUDIO_FINGERPRINT,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.TEMPO, 80u, R.string.field_key_tempo , MetadataCategory.AUDIO_PROPERTIES,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.SINGLE_DISC_TRACK_NO, 80u, R.string.field_key_single_disc_track_no , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_LYRICS_SITE, 80u, R.string.field_key_url_lyrics_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.none) },
        lazy { TrackMetadata.MultipleValues(FieldKey.ORCHESTRA, 80u, R.string.field_key_orchestra , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Orchestras can have multiple values
        lazy { TrackMetadata.MultipleValues(FieldKey.PRODUCER_SORT, 80u, R.string.field_key_producer_sort , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Producer sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, 80u, R.string.field_key_musicbrainz_release_country , MetadataCategory.MUSICBRAINZ,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.BPM, 70u, R.string.field_key_bpm , MetadataCategory.AUDIO_PROPERTIES,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_OFFICIAL_ARTIST_SITE, 70u, R.string.field_key_url_official_artist_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.none) },
        lazy { TrackMetadata.SingleValue(FieldKey.IS_SOUNDTRACK, 70u,  R.string.field_key_is_soundtrack , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_VALENCE, 70u, R.string.field_key_mood_valence , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.ORCHESTRA_SORT, 70u, R.string.field_key_orchestra_sort , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Orchestra sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, 70u, R.string.field_key_musicbrainz_release_group_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.FBPM, 60u, R.string.field_key_fbpm , MetadataCategory.AUDIO_PROPERTIES,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_OFFICIAL_RELEASE_SITE, 60u, R.string.field_key_url_official_release_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.none) },
        lazy { TrackMetadata.MultipleValues(FieldKey.PERIOD, 60u, R.string.field_key_period , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Periods can have multiple values
        lazy { TrackMetadata.MultipleValues(FieldKey.PART, 60u, R.string.field_key_part , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Parts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASE_STATUS, 60u, R.string.field_key_musicbrainz_release_status , MetadataCategory.MUSICBRAINZ,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.ORIGINAL_ALBUM, 50u, R.string.field_key_original_album , MetadataCategory.ALBUM,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.TRACK, 50u, R.string.field_key_track , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.DISC_NO, 50u, R.string.field_key_disc_no , MetadataCategory.TRACK, IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.SCRIPT, 50u, R.string.field_key_script , MetadataCategory.ADDITIONAL_CONTENT,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, 50u, R.string.field_key_url_wikipedia_artist_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.wikipedia) },
        lazy { TrackMetadata.SingleValue(FieldKey.PART_NUMBER, 50u, R.string.field_key_part_number , MetadataCategory.CREDITS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, 50u, R.string.field_key_musicbrainz_release_track_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_ACOUSTIC, 45u, R.string.field_key_mood_acoustic , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.ORIGINALRELEASEDATE, 40u, R.string.field_key_originalreleasedate , MetadataCategory.ALBUM,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.TRACK_TOTAL, 40u, R.string.field_key_track_total , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.DISC_SUBTITLE, 40u, R.string.field_key_disc_subtitle , MetadataCategory.TRACK,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, 40u, R.string.field_key_url_wikipedia_release_site , MetadataCategory.ADDITIONAL_CONTENT,TrackMetadataInputType.WebsiteWithNoRecommendation.wikipedia) },
        lazy { TrackMetadata.MultipleValues(FieldKey.PERIOD, 40u,  R.string.field_key_period , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Periods can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_AGGRESSIVE, 40u, R.string.field_key_mood_aggressive , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.CLASSICAL_NICKNAME, 40u, R.string.field_key_classical_nickname , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.PART_TYPE, 40u, R.string.field_key_part_type , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_RELEASE_TYPE, 40u, R.string.field_key_musicbrainz_release_type , MetadataCategory.MUSICBRAINZ,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_AROUSAL, 35u, R.string.field_key_mood_arousal , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.ORIGINAL_YEAR, 30u, R.string.field_key_original_year , MetadataCategory.ALBUM,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.DISC_TOTAL, 30u, R.string.field_key_disc_total , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_DANCEABILITY, 30u, R.string.field_key_mood_danceability , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.CHOIR, 30u, R.string.field_key_choir , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Choirs can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.QUALITY, 30u, R.string.field_key_quality , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_TRACK_ID, 30u, R.string.field_key_musicbrainz_track_id , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_ELECTRONIC, 25u, R.string.field_key_mood_electronic , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.RECORDINGDATE, 25u, R.string.field_key_recordingdate , MetadataCategory.CREDITS,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.RECORDINGLOCATION, 25u, R.string.field_key_recordinglocation , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOVEMENT, 20u, R.string.field_key_movement , MetadataCategory.TRACK,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_HAPPY, 20u, R.string.field_key_mood_happy , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.CHOIR_SORT, 20u, R.string.field_key_choir_sort , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Choir sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.RANKING, 20u, R.string.field_key_ranking , MetadataCategory.CREDITS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.QUALITY, 30u, R.string.field_key_quality , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_ELECTRONIC, 25u, R.string.field_key_mood_electronic , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.RECORDINGLOCATION, 25u, R.string.field_key_recordinglocation , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOVEMENT, 20u, R.string.field_key_movement , MetadataCategory.TRACK,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.CHOIR_SORT, 20u, R.string.field_key_choir_sort , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Choir sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.RANKING, 20u, R.string.field_key_ranking , MetadataCategory.CREDITS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_WORK, 20u, R.string.field_key_musicbrainz_work , MetadataCategory.MUSICBRAINZ,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_INSTRUMENTAL, 15u, R.string.field_key_mood_instrumental , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOVEMENT_NO, 10u, R.string.field_key_movement_no , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.IS_LIVE, 10u,  R.string.field_key_is_live , MetadataCategory.CLASSIFICATIONS,CheckBoxWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_PARTY, 10u, R.string.field_key_mood_party , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.MultipleValues(FieldKey.COMPOSER, 10u, R.string.field_key_composer , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) }, // Composers can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.SECTION, 10u, R.string.field_key_section , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MUSICBRAINZ_WORK_ID, 10u, R.string.field_key_musicbrainz_work_id , MetadataCategory.MUSICBRAINZ,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOVEMENT_TOTAL, 5u, R.string.field_key_movement_total , MetadataCategory.TRACK,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_RELAXED, 5u, R.string.field_key_mood_relaxed , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.MOOD_SAD, 5u, R.string.field_key_mood_sad , MetadataCategory.CLASSIFICATIONS,IntegerWithNoRecommendation) },
        lazy { TrackMetadata.MultipleValues(FieldKey.COMPOSER_SORT, 5u, R.string.field_key_composer_sort , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) }, // Composer sorts can have multiple values
        lazy { TrackMetadata.SingleValue(FieldKey.RECORD_LABEL, 5u, R.string.field_key_record_label , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.RECORDINGSTARTDATE, 5u, R.string.field_key_recordingstartdate , MetadataCategory.CREDITS,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.RECORDINGENDDATE, 5u, R.string.field_key_recordingenddate , MetadataCategory.CREDITS,DateWithNoRecommendation) },
        lazy { TrackMetadata.SingleValue(FieldKey.WORK, 5u, R.string.field_key_work , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.WORK_TYPE, 5u, R.string.field_key_work_type , MetadataCategory.CREDITS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.CONDUCTOR, 4u, R.string.field_key_conductor , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.CONDUCTOR_SORT, 3u, R.string.field_key_conductor_sort , MetadataCategory.CLASSIFICATIONS,
            TextWithNoRecommendation
        ) },
        lazy { TrackMetadata.SingleValue(FieldKey.PART, 1u, R.string.field_key_part , MetadataCategory.TRACK,
            TextWithNoRecommendation
        ) },
    )
    var anyFieldChanged by mutableStateOf(false)
        private set

    fun<T> updateAnyFieldChanged(old : T,new : T) {
        if (old != new) anyFieldChanged = true
    }

    fun copyIntoTrackDetails(trackDetails: TrackDetails): TrackDetails? {
        fun FieldKey.firstOrNull() = metadataFields.firstOrNull { it.isInitialized() && it.value.fieldKey == this }?.value
        val name = FieldKey.TRACK.firstOrNull() as? TrackMetadata.SingleValue
        val genre = FieldKey.GENRE.firstOrNull() as? TrackMetadata.SingleValue
        val album = FieldKey.ALBUM.firstOrNull() as? TrackMetadata.SingleValue
        val albumArtists = FieldKey.ALBUM_ARTISTS.firstOrNull() as? TrackMetadata.MultipleValues
        val artists = FieldKey.ARTISTS.firstOrNull() as? TrackMetadata.MultipleValues
        // TODO : Allow editing of artwork -> Search on internet
        val artwork = null

        return if(name == null && genre == null && album == null && albumArtists == null && artists == null && artwork== null) null
        else {
            TrackDetails(
                name = name?.defaultValue ?: trackDetails.name,
                artists = if(artists != null && artists.readInitialDefaultValue) artists.defaultValue.joinToString(",") else trackDetails.artists,
                albumArtists = if(albumArtists != null && albumArtists.readInitialDefaultValue) albumArtists.defaultValue.joinToString(",") else trackDetails.albumArtists,
                genre = genre?.defaultValue ?: trackDetails.genre,
                album = album?.defaultValue ?: trackDetails.album,
                artwork = null ?: trackDetails.artwork
            )
        }
    }

    fun updateMetadata(coroutineScope : CoroutineScope, snackBarState : SnackbarHostState, tag : Tag) = metadataFields.forEach {
        val value = it.value

        if (it.isInitialized()) {
            when {
                value.inputType is CheckBoxWithNoRecommendation -> {
                    tag.setField(
                        value.fieldKey,
                        if((value as TrackMetadata.SingleValue).currentValue.value == null) "false" else "true"
                    )

                }
                value.inputType is TrackMetadataInputType.WebsiteWithNoRecommendation && value.inputType.isError -> {
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            "There's an error with the ${value.fieldKey} field. Please correct it before saving this field."
                        )
                    }
                }
                value is TrackMetadata.SingleValue && value.currentValue.value != null -> tag.setField(
                    value.fieldKey,
                    value.currentValue.value
                )
                value is TrackMetadata.MultipleValues && value.readInitialDefaultValue -> tag.setField(
                    value.fieldKey,
                    *value.currentValue.toTypedArray()
                )
            }
        }
    }
}