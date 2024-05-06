package com.deathsdoor.chillback.core.preference

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@OptIn(ExperimentalSettingsApi::class)
object ApplicationPreference {
    private val settings = RuntimeObservableSettingsWrapper(delegate = Settings())
    operator fun getValue(_nothing: Nothing?, property: KProperty<*>): FlowSettings {
        return settings.toFlowSettings()
    }
}