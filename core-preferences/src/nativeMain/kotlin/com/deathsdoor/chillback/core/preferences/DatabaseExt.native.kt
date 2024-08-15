package com.deathsdoor.chillback.core.preferences

import androidx.room.Room
import androidx.room.RoomDatabase

actual fun databaseBuilder(): RoomDatabase.Builder<AppLocalDatabase> = Room.databaseBuilder(
    name =  NSHomeDirectory() + "/${AppLocalDatabase.database}",
    factory =  { AppLocalDatabase::class.instantiateImpl() }
)