package com.deathsdoor.chillback.data.music

actual suspend fun <T> AstroPlayer.withContext(value: suspend () -> T): T = value()