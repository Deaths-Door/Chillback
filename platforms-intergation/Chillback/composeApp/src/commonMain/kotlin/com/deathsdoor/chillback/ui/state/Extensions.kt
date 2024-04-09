package com.deathsdoor.chillback.ui.state

import com.deathsdoor.chillback.data.music.AstroPlayer

expect suspend fun createAstroPlayer() : AstroPlayer
expect fun ChillbackApplicationState.onKmpCleared()