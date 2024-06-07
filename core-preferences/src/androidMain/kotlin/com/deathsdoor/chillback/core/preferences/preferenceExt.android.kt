package com.deathsdoor.chillback.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.startup.Initializer

// https://developer.android.com/kotlin/multiplatform/datastore#common
internal actual fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = { applicationContext.filesDir.resolve(dataStoreFileName).absolutePath }
)


internal lateinit var applicationContext: Context
    private set

// https://proandroiddev.com/how-to-avoid-asking-for-android-context-in-kotlin-multiplatform-libraries-api-d280a4adebd2
internal class ContextInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}