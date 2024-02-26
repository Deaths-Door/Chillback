package com.deathsdoor.chillback.data.extensions

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.repositories.MusicRepository

fun <T> MediaController.mapMediaItems(transform : (MediaItem) -> T) = (0 until mediaItemCount).map { transform(getMediaItemAt(it)) }

suspend fun Collection<Track>.asMediaItems(musicRepository : MusicRepository) = map { it.asMediaItem(musicRepository) }