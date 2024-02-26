package com.deathsdoor.chillback.data.media

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession.QueueItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

// Extend MediaSessionService
class MediaPlaybackService(private val playerClosure: Context.() -> ExoPlayer = { ExoPlayer.Builder(this).build() }) : MediaSessionService() {
    private lateinit var mediaSession: MediaSession

    // Create your player and media session in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        val exoplayer = playerClosure().apply { prepare() }
        mediaSession = MediaSession.Builder(this,exoplayer).build()
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession.player.run {
            if(playWhenReady) pause()
        }

        stopSelf()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    fun createMediaController(
        context: Context,
        customize: MediaController.Builder.() -> MediaController.Builder = { this }
    ): ListenableFuture<MediaController> {
        val sessionToken = SessionToken(context, ComponentName(context, this::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).customize().buildAsync()
        controllerFuture.addListener({ controllerFuture.get().prepare() },MoreExecutors.directExecutor())
        return controllerFuture
    }
}