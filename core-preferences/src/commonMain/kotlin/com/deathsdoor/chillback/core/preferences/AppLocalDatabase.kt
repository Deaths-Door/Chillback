package com.deathsdoor.chillback.core.preferences

abstract class AppLocalDatabase {
    companion object {
        internal const val databaseName = "chillback-database.db"
        val database : AppLocalDatabase by lazy { TODO("Implement application local database using room") }
    }
}