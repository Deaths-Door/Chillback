package com.deathsdoor.chillback.data.database

import app.cash.sqldelight.ColumnAdapter
import com.benasher44.uuid.Uuid
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import com.eygraber.uri.Uri

object ApplicationDatabase {
    @Volatile
    private var _defaultdatabase : ApplicationLocalDatabase? = null
        get() = field ?: synchronized(this) {
            _defaultdatabase = ApplicationLocalDatabase(
                driver = createSqlDriver(),
                TrackAdapter = Track.Adapter(
                    idAdapter = object : ColumnAdapter<Uuid, String>{
                        override fun decode(databaseValue: String): Uuid = Uuid.fromString(databaseValue)
                        override fun encode(value: Uuid): String = value.toString()
                    },
                    sourceAdapter = object : ColumnAdapter<Uri, String> {
                        override fun decode(databaseValue: String): Uri = Uri.parse(databaseValue)
                        override fun encode(value: Uri): String = value.toString()
                    }
                )
            )
            _defaultdatabase
        }
    private val defaultDatabase get() = _defaultdatabase!!

    val showDataLocally get() = Firebase.auth.currentUser == null
    fun<T> preferredDatabase(
        onDefault : (ApplicationLocalDatabase) -> T,
        onPreferred : (FirebaseDatabase) ->  T
    ) = if(showDataLocally) onDefault(defaultDatabase)
    else onPreferred(Firebase.database)

}