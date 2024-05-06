package com.deathsdoor.chillback.data.preferences

import kotlinx.coroutines.flow.Flow

/// Helper Interface
interface Settings<T> {
    fun current() : Flow<T>
    suspend fun update(value : T)
}