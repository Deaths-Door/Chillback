package com.deathsdoor.chillback.data

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.NativeMediaPLayer
import com.deathsdoor.chillback.ui.ChillbackState

@Composable
internal actual fun rememberChillbackState(): ChillbackState {
    return viewModel {
        val nativeMediaPlayer = NativeMediaPLayer()
        val astroPlayer = AstroPlayer(nativeMediaPlayer)

        ChillbackState(
            astroPlayer = astroPlayer
        )
    }
}