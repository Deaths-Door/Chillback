package com.deathsdoor.chillback.data

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.FirebasePlatform
import com.google.firebase.initialize

internal fun initializeFirebase() {
    // https://github.com/Kotlin/kotlinx.coroutines/issues/3914#issuecomment-1937903472
    System.setProperty("kotlinx.coroutines.fast.service.loader", "false")

    // TODO : implement this correctly https://github.com/GitLiveApp/firebase-java-sdk by using custom datastore
    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
        val storage = mutableMapOf<String, String>()
        override fun store(key: String, value: String) = storage.set(key, value)
        override fun retrieve(key: String) = storage[key]
        override fun clear(key: String) { storage.remove(key) }
        override fun log(msg: String) = println(msg)
    })

    FirebaseApp.initializeApp(
        /* context = */ Application() as Context,
        /* options = */ FirebaseOptions.Builder()
            .setApiKey("AIzaSyCcFtfjM_i77FRVyVTlD3KzPZNvdjt6Wzc")
            .setApplicationId("1:756199310380:web:8c1034419ac63436afbd89")
            .setDatabaseUrl("https://chillback-a93e5-default-rtdb.firebaseio.com")
            .setProjectId("chillback-a93e5")
            .setStorageBucket("chillback-a93e5.appspot.com")
            .build()
    )
}
