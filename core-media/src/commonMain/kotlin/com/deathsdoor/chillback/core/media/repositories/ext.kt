package com.deathsdoor.chillback.core.media.repositories

import com.deathsdoor.astroplayer.core.AstroMediaMetadata

val AstroMediaMetadata.duration get() = extras?.get("duration") as? Int

internal fun MutableMap<String,Any>.updateDuration(value : Int) {
    this["duration"] = value
}
