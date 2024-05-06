package com.deathsdoor.chillback.data.backgroundtasks

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.deathsdoor.chillback.data.music.AstroPlayer
import com.deathsdoor.chillback.data.music.MediaPlaybackPreferences
import com.deathsdoor.chillback.data.preferences.ApplicationPreferences
import com.deathsdoor.chillback.data.preferences.playback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MediaPlaybackPreferenceWorkManager(context : Context,workerParameters: WorkerParameters) : Worker(context,workerParameters) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun doWork(): Result {
        scope.launch {
            ApplicationPreferences().playback.update(Json.decodeFromString(
                this@MediaPlaybackPreferenceWorkManager.inputData.getString(DATA)!!
            ))
        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        job.cancel()
    }

    companion object {
        private const val DATA = "data"
        private const val NAME = "saving-music-playback-preferences"

        fun start(context: Context, mediaController: AstroPlayer) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequestBuilder<MediaPlaybackPreferenceWorkManager>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setInputData(
                            Data.Builder()
                                .putString(DATA, Json.encodeToString(MediaPlaybackPreferences.from(mediaController)))
                                .build()
                        )
                        .build()
                )
        }
    }
}