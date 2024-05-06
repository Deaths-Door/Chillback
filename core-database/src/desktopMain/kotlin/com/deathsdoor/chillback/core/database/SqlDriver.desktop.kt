package com.deathsdoor.chillback.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.deathsdoor.core.database.AppLocalDatabase

actual fun createSqlDriver(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    AppLocalDatabase.Schema.create(driver)
    return driver
}