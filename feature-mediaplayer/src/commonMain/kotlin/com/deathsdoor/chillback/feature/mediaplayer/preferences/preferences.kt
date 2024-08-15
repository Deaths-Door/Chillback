package com.deathsdoor.chillback.feature.mediaplayer.preferences

import com.deathsdoor.astroplayer.core.equalizer.EqualizerPresets
import com.deathsdoor.astroplayer.core.equalizer.EqualizerValues
import com.deathsdoor.chillback.core.preferences.ApplicationDatabase
import com.deathsdoor.chillback.core.preferences.ApplicationDatabase.preferredDatabase
import com.deathsdoor.chillback.core.preferences.ApplicationPreference
import com.deathsdoor.chillback.core.preferences.ApplicationPreference.preferredCurrent
import com.deathsdoor.chillback.core.preferences.Settings
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

private fun DatabaseReference.presets() = child("equalizer-presets")

@OptIn(ExperimentalCoroutinesApi::class)
internal val ApplicationDatabase.equalizerPresets: Flow<List<EqualizerValues>> get() = preferredDatabase(
    local = { TODO("implement this") },
    online = { reference ->
        reference.presets().valueEvents.mapLatest { snapshot ->
            snapshot.children.map { it.value() }
        }
    }
)