package com.deathsdoor.chillback.data.models

import androidx.annotation.StringRes
import com.deathsdoor.chillback.R


enum class MetadataCategory(@StringRes val stringId : Int) {
    AUDIO_FINGERPRINT(R.string.sort_by_audio_fingerprint),
    ALBUM(R.string.sort_by_album),
    AUDIO_PROPERTIES(R.string.sort_by_audio_properties),
    ARTIST(R.string.sort_by_artist),
    ADDITIONAL_CONTENT(R.string.sort_by_additional_content),
    CLASSIFICATIONS(R.string.sort_by_classifications),
    CREDITS(R.string.sort_by_credits),
    MUSICBRAINZ(R.string.sort_by_musicbrainz),
    TRACK(R.string.sort_by_track)
}