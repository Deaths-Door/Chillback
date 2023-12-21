package com.deathsdoor.chillback.data.mediaplayer

import android.content.ComponentName
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

// Extend MediaSessionService
open class MediaPlaybackService(
    private val player: Context.() -> ExoPlayer = { ExoPlayer.Builder(this).build() }
) : MediaSessionService() {
    private lateinit var mediaSession: MediaSession

    // Create your Player and MediaSession in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, player()).build()
    }

    // Return a MediaSession to link with the MediaController that is making
    // this request.
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    open fun createMediaController(
        context: Context,
        customize: MediaController.Builder.() -> MediaController.Builder = { this }
    ): ListenableFuture<MediaController> {
        val sessionToken = SessionToken(context, ComponentName(context, this::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).customize().buildAsync()
        controllerFuture.addListener({ controllerFuture.get().prepare() },MoreExecutors.directExecutor())
        return controllerFuture
    }
}