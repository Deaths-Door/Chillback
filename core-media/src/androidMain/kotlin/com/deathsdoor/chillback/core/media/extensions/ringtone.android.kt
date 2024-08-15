package com.deathsdoor.chillback.core.media.extensions

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableFloatState
import androidx.core.net.toFile
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.SessionState
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroMediaMetadata
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.core.media.repositories.MusixRepository
import com.deathsdoor.chillback.core.media.resources.Res
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

suspend fun Context.updateRingtone(
    resource: LazyResourceLoader,
    contentDuration: Long,
    mediaItem: AstroMediaItem,
    stackableSnackbarState: StackableSnackbarState,
    startProgress: MutableFloatState,
    finishProgress: MutableFloatState,
) =
    when(
        val metadata = MusixRepository.readMostImportantFields(
            resource = resource,
            mediaItem = mediaItem,
            stackableSnackbarState = stackableSnackbarState
            )
    ) {
        null -> stackableSnackbarState.showRingtoneFailure(
            resource,
            Res.strings.failed_setting_ringtone_fetch_metadata
        )
        else -> {
            val inputFilePath = mediaItem.source.toString()
            val outputFilePath = createOutputFilePath()

            val isSuccessful = inputFilePath.trimAudioTo(
                outputFilePath = outputFilePath,
                contentDuration = contentDuration,
                startProgress = startProgress,
                finishProgress = finishProgress,
            )

            when(isSuccessful){
                true -> {
                    deletePreviousRingtoneFile()
                    setDeviceRingtone(outputFilePath = outputFilePath, metadata = metadata)

                    stackableSnackbarState.showSuccessSnackbar(
                        title = resource.stringResource(Res.strings.success_setting_ringtone)
                    )
                }
                false -> stackableSnackbarState.showRingtoneFailure(resource,Res.strings.failed_setting_ringtone_crop_song)
            }
        }
    }

private fun StackableSnackbarState.showRingtoneFailure(resource: LazyResourceLoader,cause : StringResource) = showErrorSnackbar(
    title = resource.stringResource(Res.strings.failed_setting_ringtone),
    description = resource.stringResource(cause)
)


private suspend fun String.trimAudioTo(
    outputFilePath : String,
    contentDuration: Long,
    startProgress: MutableFloatState,
    finishProgress: MutableFloatState,
): Boolean {

    // Trim the audio file from the desired start to the desired end, and create a internal file for it which is set as the ringtone
    val session = FFmpegKit.execute(
"""-ss ${contentDuration * startProgress.floatValue} -to ${contentDuration * finishProgress.floatValue} -i "$this" -c copy $outputFilePath"""
    )


    /*loop {
        val state = session.state
        when(state) {
            SessionState.CREATED , SessionState.RUNNING -> continue
            SessionState.FAILED -> ("report this to the user")
            SessionState.COMPLETED -> {
                val code = session.returnCode
                when {
                    code.isValueSuccess ->
                    code.isValueError -> ("report this to the user")
                    code.isValueCancel -> ("report this to the user")
                }
            }
        }
    }*/
    while(true) {
        val state = session.state

        when {
            state == SessionState.CREATED || state == SessionState.RUNNING -> {
                delay(500L)
                continue
            }
            state == SessionState.COMPLETED && session.returnCode.isValueSuccess -> return true
            else -> return false
        }
    }
}

private fun Context.createOutputFilePath(): String {
    val currentMoment: Instant = Clock.System.now()
    val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    // write to internal storage - https://stackoverflow.com/a/25905716/20243803
    return "${filesDir.absolutePath}/ringtone_$datetimeInSystemZone.mp3"
}

private fun Context.deletePreviousRingtoneFile() {
    val uri = RingtoneManager.getActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_RINGTONE
    )

    val file = uri.toFile()

    // From createOutputFilePath()
    if(file.exists() && file.startsWith(filesDir.absolutePath) && file.nameWithoutExtension.contains("ringtone_")) {
        file.delete()
    }
}


private fun Context.setDeviceRingtone(outputFilePath : String,metadata : AstroMediaMetadata) {
    val contextValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DATA, outputFilePath)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")

        put(MediaStore.MediaColumns.TITLE,metadata.displayTitle)
        put(MediaStore.Audio.Media.ARTIST, metadata.artist)
        put(MediaStore.Audio.Media.ALBUM, metadata.albumTitle)

        put(MediaStore.Audio.Media.IS_RINGTONE, true)
        put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
        put(MediaStore.Audio.Media.IS_ALARM, true)
        put(MediaStore.Audio.Media.IS_MUSIC, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            put(MediaStore.Audio.Media.GENRE, metadata.genre)
        }
    }

    val ringtoneUri = contentResolver.insert(
        MediaStore.Audio.Media.getContentUriForPath(outputFilePath)!!,
        contextValues
    )

    RingtoneManager.setActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_RINGTONE,
        ringtoneUri
    )
}