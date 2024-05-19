package com.deathsdoor.chillback.core.preferences

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database

object ApplicationDatabase {
    /**
     *  An object for retrieving or storing data using either local or online database depending on user authentication.
     *
     *  This object provides a convenient way to access the appropriate database based on user authentication status.
     *  If a user is currently signed in, it prioritizes using Firebase Realtime Database (https://firebase.google.com/docs/database).
     *  Otherwise, it falls back to using the local `AppLocalDatabase`.
     *
     *  @param local A lambda function that takes the `AppLocalDatabase` instance and returns the desired data or performs the desired operation.
     *  @param online A lambda function that takes the `FirebaseDatabase` instance and returns the desired data or performs the desired operation.
     *  @return The result of the lambda function executed on the appropriate database instance (local or online).
     */
    inline fun<T> preferredDatabase(
        local : (AppLocalDatabase) -> T,
        online : (DatabaseReference) ->  T
    ) = if(Firebase.auth.currentUser == null) local(AppLocalDatabase.database)
    else online(Firebase.database.reference(Firebase.auth.currentUser!!.uid))
}