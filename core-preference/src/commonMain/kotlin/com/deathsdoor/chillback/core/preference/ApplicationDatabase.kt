package com.deathsdoor.chillback.core.preference

import com.deathsdoor.core.database.AppLocalDatabase
import com.deathsdoor.core.database.createSqlDriver
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlin.concurrent.Volatile

object ApplicationDatabase {
    private val lock = SynchronizedObject()

    @Volatile
    private var _defaultdatabase : AppLocalDatabase? = null
        get() = field ?: synchronized(lock) {
            _defaultdatabase = AppLocalDatabase(
                driver = createSqlDriver(),
            )
            _defaultdatabase
        }

    private val defaultDatabase get() = _defaultdatabase!!

    private val storeDataLocally get() = Firebase.auth.currentUser == null

    fun<T> preferredDatabase(
        onDefault : (AppLocalDatabase) -> T,
        onPreferred : (FirebaseDatabase) ->  T
    ) = if(storeDataLocally) onDefault(defaultDatabase)
    else onPreferred(Firebase.database)
}