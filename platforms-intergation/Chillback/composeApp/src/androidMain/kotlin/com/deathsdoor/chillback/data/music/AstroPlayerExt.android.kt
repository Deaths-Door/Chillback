package com.deathsdoor.chillback.data.music

import kotlinx.coroutines.Dispatchers

actual suspend fun <T> AstroPlayer.withContext(value: suspend () -> T): T = kotlinx.coroutines.withContext(Dispatchers.Main) {
    value()
}