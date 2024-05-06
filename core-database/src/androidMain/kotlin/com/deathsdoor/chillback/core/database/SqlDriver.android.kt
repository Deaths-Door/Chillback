package com.deathsdoor.chillback.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.deathsdoor.core.database.AppLocalDatabase

actual fun createSqlDriver(): SqlDriver {
    return AndroidSqliteDriver(AppLocalDatabase.Schema, applicationContext, "chillback-database.db")
}