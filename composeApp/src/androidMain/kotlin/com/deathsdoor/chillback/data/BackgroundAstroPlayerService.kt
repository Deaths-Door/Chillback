package com.deathsdoor.chillback.data

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class BackgroundAstroPlayerService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.let {
            if(it.playWhenReady) it.pause()
        }

        stopSelf()
    }

    // Create your player and media session in the onCreate lifecycle event
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val exoplayer = ExoPlayer.Builder(this)
            .setDeviceVolumeControlEnabled(true)
            .build()

        exoplayer.prepare()
        mediaSession = MediaSession.Builder(this,exoplayer).build()
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.let {
            it.player.release()
            it.release()
        }

        super.onDestroy()
    }
}