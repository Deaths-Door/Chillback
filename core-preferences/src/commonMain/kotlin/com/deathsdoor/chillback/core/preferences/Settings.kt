package com.deathsdoor.chillback.core.preferences

import kotlinx.coroutines.flow.Flow

interface Settings<T> {
    fun current() : Flow<T>
    suspend fun update(value : T)
}