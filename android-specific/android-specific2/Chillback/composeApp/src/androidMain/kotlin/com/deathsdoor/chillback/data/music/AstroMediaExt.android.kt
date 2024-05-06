package com.deathsdoor.chillback.data.music

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.eygraber.uri.toAndroidUri
import com.eygraber.uri.toUri


fun AstroMediaItem.asNativeMediaItem(metadata: MediaMetadata = this.metadata.asNativeMediaMetadata()): MediaItem = asNativeMediaItemBuilder(metadata).build()
fun AstroMediaItem.asNativeMediaItemBuilder(metadata: MediaMetadata = this.metadata.asNativeMediaMetadata()): MediaItem.Builder {
    return MediaItem.Builder()
        .setMediaId(mediaId)
        .setMediaMetadata(metadata)
        .setUri(source.toAndroidUri())
}

fun AstroMediaMetadata.asNativeMediaMetadata() : MediaMetadata = asNativeMediaMetadataBuilder().build()
fun AstroMediaMetadata.asNativeMediaMetadataBuilder() : MediaMetadata.Builder {
    return MediaMetadata.Builder().apply {
        setTitle(title)
        setArtist(artist)
        setAlbumTitle(albumTitle)
        setAlbumArtist(albumArtist)
        setDisplayTitle(displayTitle)
        setSubtitle(subtitle)
        setTrackNumber(trackNumber)
        setTotalTrackCount(totalTrackCount)
        setRecordingYear(recordingYear)
        setRecordingMonth(recordingMonth)
        setRecordingDay(recordingDay)
        setReleaseYear(releaseYear)
        setReleaseMonth(releaseMonth)
        setReleaseDay(releaseDay)
        setWriter(writer)
        setComposer(composer)
        setConductor(conductor)
        setDiscNumber(discNumber)
        setTotalDiscCount(totalDiscCount)
        setGenre(genre)
        setCompilation(compilation)
        setStation(station)
        Bundle()
        extras?.let {
            setExtras(Bundle.EMPTY.apply {
                putSerializable("extras",it)
            })
        }
    }
}

fun MediaItem.asAstroMediaItem(): AstroMediaItem? {
    return requestMetadata.mediaUri?.toUri()?.let {
        AstroMediaItem(
        mediaId = mediaId,
        source = it,
        metadata =  mediaMetadata.asAstroMediaMetadata()
        )
    }
}

fun MediaMetadata.asAstroMediaMetadata(): AstroMediaMetadata {
    return AstroMediaMetadata(
        title = title,
        artist = artist,
        albumTitle = albumTitle,
        albumArtist = albumArtist,
        displayTitle = displayTitle,
        subtitle = subtitle,
        description = description,
        trackNumber = trackNumber,
        totalTrackCount = totalTrackCount,
        recordingYear = recordingYear,
        recordingMonth = recordingMonth,
        recordingDay = recordingDay,
        releaseYear = releaseYear,
        releaseMonth = releaseMonth,
        releaseDay = releaseDay,
        writer = writer,
        composer = composer,
        conductor = conductor,
        discNumber = discNumber,
        totalDiscCount = totalDiscCount,
        genre = genre,
        compilation = compilation,
        station = station,
        extras = extras?.getSerializable("extras")
    )
}

