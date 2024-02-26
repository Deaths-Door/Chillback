package com.deathsdoor.chillback.data.database

import android.content.Context
import android.os.Debug
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionCrossReference


@Database(
    version = 8,
    entities = [Track::class, TrackCollection::class,TrackCollectionCrossReference::class]
)
@TypeConverters(
    value = [UriTypeConvertor::class]
)
abstract class ApplicationLocalDatabase : RoomDatabase() {
    abstract val trackDao : TrackDao
    abstract val trackCollectionDao : TrackCollectionDao
    abstract val trackCollectionCrossReferenceDao : TrackCollectionCrossReferenceDao
    companion object {
        @Volatile
        private var database : ApplicationLocalDatabase? = null

        operator fun invoke(context: Context): ApplicationLocalDatabase {
            return database ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationLocalDatabase::class.java,
                    "chillback-database"
                )  // TODO : REMOVE THIS
                .fallbackToDestructiveMigration()
                .build()

                database = instance
                return database!!
            }
        }
    }
}