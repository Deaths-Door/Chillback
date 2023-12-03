package com.deathsdoor.chillback.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import com.deathsdoor.chillback.data.mediaplayer.MediaPlaybackService
import com.google.common.util.concurrent.ListenableFuture
import java.io.Closeable

class CoreViewModel constructor(context: Context) : ViewModel() {
    private var controllerFuture: ListenableFuture<MediaController> = MediaPlaybackService().createMediaController(context)
    val controller : MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null
}