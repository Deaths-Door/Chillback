package com.deathsdoor.chillback.core.preferences

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty


@OptIn(ExperimentalSettingsApi::class)
object ApplicationPreference {
    @PublishedApi
    internal val settings = RuntimeObservableSettingsWrapper(delegate = Settings()).toFlowSettings()

    operator fun getValue(_nothing: Nothing?, property: KProperty<*>): FlowSettings = settings

    inline fun<reified T : Any> currentLocal(receiveFlow : (FlowSettings) -> Flow<T>): Flow<T> = receiveFlow(settings)
    inline fun updateLocal(update : (FlowSettings) -> Unit) {
        update(settings)
    }
}