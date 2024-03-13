package com.deathsdoor.chillback.data.models

enum class MetadataCategory(val displayName: String) {
    AUDIO_FINGERPRINT("Audio Identifiers"),
    ALBUM("Album Information"),
    AUDIO_PROPERTIES("Audio Properties"),
    ARTIST("Artist Information"),
    ADDITIONAL_CONTENT("Additional Content"),
    CLASSIFICATIONS("Classifications"),
    CREDITS("Credits"),
    MUSICBRAINZ("Musicbrainz"),
    TRACK("Track Information"),
}