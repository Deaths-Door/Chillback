package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsSwitch
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.core.equalizer.EqualizerPresets
import com.deathsdoor.astroplayer.core.equalizer.EqualizerValues
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.astroplayer.ui.equalizer.EqualizerGraph
import com.deathsdoor.astroplayer.ui.equalizer.rememberEqualizerGraphState
import com.deathsdoor.chillback.core.layout.actions.BackButton
import com.deathsdoor.chillback.core.preferences.ApplicationDatabase
import com.deathsdoor.chillback.feature.mediaplayer.preferences.equalizerPresets
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun EqualizerScreen(state : AstroPlayerState,navController: NavController) = Scaffold(
    topBar = {
        TopAppBar(
            navigationIcon = { BackButton { navController.popBackStack() } },
            title = { Text(stringResource(Res.strings.equalizer)) },
            actions = {
                Switch(
                    checked = state.isEqualizerEnabled,
                    onCheckedChange = { state.astroPlayer.isEqualizerEnabled = !state.isEqualizerEnabled }
                )
            }
        )
    },
    content = { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SettingsSwitch(
                state = state.isEqualizerEnabled,
                onCheckedChange = { state.astroPlayer.isEqualizerEnabled = it },
                title = { Text(text = stringResource(Res.strings.equalizer_enabled)) }
            )

            SettingsSwitch(
                state = state.isSmartEqualizerEnabled,
                onCheckedChange = { state.astroPlayer.isSmartEqualizerEnabled = it },
                title = { Text(text = stringResource(Res.strings.smart_equalizer_enabled)) },
                subtitle = { Text(text = stringResource(Res.strings.smart_equalizer_description)) }
            )

            var selectedEqualizerValues by remember { mutableStateOf<EqualizerValues?>(null) }
            val graphState = rememberEqualizerGraphState(selectedEqualizerValues ?: state.currentEqualizerValues ?: EqualizerPresets.Default)

            val _1weight =Modifier.weight(1f)
            ElevatedCard(modifier = _1weight) {
                // TODO; save user values + button
                EqualizerGraph(
                    state = graphState,
                    enabled = state.isEqualizerEnabled
                )
            }

            // TODO : replace this with flow row + max rows
            LazyRow {
                for (value in EqualizerPresets.AllPresets.values) {
                    equalizerChip(
                        identifier = value.identifier,
                        state = state,
                        update = { selectedEqualizerValues = value }
                    )
                }
            }

            val customPresets by ApplicationDatabase.equalizerPresets.collectAsState(emptyList())

            if(customPresets.isNotEmpty()) {
                LazyRow {
                    for (values in customPresets) {
                        equalizerChip(
                            identifier = values.identifier,
                            state = state,
                            update = { selectedEqualizerValues = values }
                        )
                    }
                }
            }

            ReverbCard(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isEqualizerEnabled
            )

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                GenericSoundModifier(modifier = _1weight,label = Res.strings.bass_boost)
                GenericSoundModifier(modifier = _1weight,label = Res.strings.loudness)
                GenericSoundModifier(modifier = _1weight,label = Res.strings.visualizer)
            }
        }
    }
)