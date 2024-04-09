package com.deathsdoor.chillback.data.services

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.deathsdoor.chillback.backend.cleanYoutubeCache

class CacheWorkManager(context : Context, workerParameters: WorkerParameters) : Worker(context,workerParameters) {
    override fun doWork(): Result {
        // TODO : Add more 'cleaner's here and start it from where it is triggered
        cleanYoutubeCache()
        return Result.success()
    }

    companion object {
        private const val NAME =  "cache-cleaner"

        fun start(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequestBuilder<CacheWorkManager>()
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build()
                            )
                        .build()
                )
        }
    }
}