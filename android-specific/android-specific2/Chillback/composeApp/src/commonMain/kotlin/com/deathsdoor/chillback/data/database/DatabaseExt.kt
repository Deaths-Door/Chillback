package com.deathsdoor.chillback.data.database

import com.benasher44.uuid.Uuid

fun ApplicationDatabase.tracksFromUuids(ids : List<Uuid>) : List<Track> {
    return preferredDatabase(
        onDefault = { database ->
            database.transactionWithResult {
                ids.map { id -> database.trackQueries.trackWhere(id).executeAsOne() }
            }
        },
        onPreferred = {
            TODO("Implement this from firebase later")
        }
    )
}