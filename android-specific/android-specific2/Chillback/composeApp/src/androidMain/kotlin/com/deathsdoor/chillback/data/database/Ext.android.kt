package com.deathsdoor.chillback.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.deathsdoor.chillback.data.extensions.applicationContext

actual fun createSqlDriver(): SqlDriver = AndroidSqliteDriver(schema = ApplicationLocalDatabase.Schema,
    applicationContext,
    "local.db"
)