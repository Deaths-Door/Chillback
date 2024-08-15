package com.deathsdoor.chillback.data

import android.content.ComponentName
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.NativeMediaPLayer
import com.deathsdoor.chillback.ui.ChillbackState
import com.google.common.util.concurrent.MoreExecutors
import kotlin.reflect.KClass

@Composable
internal actual fun rememberChillbackState(): ChillbackState {
    val context = LocalContext.current
    val viewmodel = viewModel {
        val sessionToken = SessionToken(context, ComponentName(context, BackgroundAstroPlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({ controllerFuture.get().prepare() }, MoreExecutors.directExecutor())

        val nativeMediaPlayer = NativeMediaPLayer(controllerFuture.get())

        ChillbackState(
            astroPlayer = AstroPlayer(nativeMediaPlayer)
        )
    }

    return viewmodel
}


