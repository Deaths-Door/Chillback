package com.deathsdoor.chillback.data.extensions

import android.content.Context
import androidx.startup.Initializer


// Based on the source code from https://github.com/psuzn/multiplatform-paths/blob/develop/context-provider
/**
 * Application scoped context for current app session
 */
lateinit var applicationContext: Context

internal class ApplicationContextInitializer : Initializer<Context> {
    override fun create(context: Context): Context = context.also {
        applicationContext = it.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}