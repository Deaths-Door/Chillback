package com.deathsdoor.chillback.data.music

import com.eygraber.uri.Uri

data class AstroMediaItem(
    val mediaId : String,
    val source : Uri,
    val metadata : AstroMediaMetadata?,
)