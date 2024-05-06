package com.deathsdoor.chillback.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.deathsdoor.core.database.AppLocalDatabase

actual fun createSqlDriver(): SqlDriver {
    return NativeSqliteDriver(AppLocalDatabase.Schema, "chillback-database.db")
}