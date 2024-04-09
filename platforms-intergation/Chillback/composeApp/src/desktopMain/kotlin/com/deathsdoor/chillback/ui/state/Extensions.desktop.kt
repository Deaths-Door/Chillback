package com.deathsdoor.chillback.ui.state

import com.deathsdoor.chillback.data.music.AstroPlayer

actual suspend fun createAstroPlayer(): AstroPlayer = AstroPlayer()

/// This is handled by the UI and User
actual fun ChillbackApplicationState.onKmpCleared() = Unit