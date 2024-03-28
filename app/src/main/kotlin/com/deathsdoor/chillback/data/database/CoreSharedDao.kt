package com.deathsdoor.chillback.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference

@Dao
sealed interface CoreSharedDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(value: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAll(vararg value: T): List<Long>

    @Update
    suspend fun update(value: T)

    @Update
    suspend fun updateAll(values : List<T>)

    @Transaction
    suspend fun insertOrUpdate(value: T) {
        val id = insertOrIgnore(value)
        if (id == -1L) update(value)
    }

    @Transaction
    suspend fun insertOrUpdateAll(vararg values : T) {
        val insertResult = insertOrIgnoreAll(*values)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(values[i])
        }

        if (updateList.isNotEmpty()) updateAll(updateList)
    }
}
