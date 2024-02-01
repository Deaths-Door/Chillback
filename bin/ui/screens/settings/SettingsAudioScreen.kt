package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.components.settings.SettingsCheckboxListItem
import com.deathsdoor.chillback.ui.components.settings.SettingsCollapsingToolbar
import com.deathsdoor.chillback.ui.providers.LocalSettings
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsAudioScreen(navController: NavHostController) = SettingsCollapsingToolbar(
    navController = navController,
    text = SettingScreenRoutes.Audio.title,
    body = {
        val settings = LocalSettings.current
        val coroutineScope = rememberCoroutineScope()

        val playbackVolumeGroups = playbackVolumeGroups(settings)

        LazyColumn {
            item { DataSaverGroup(coroutineScope = coroutineScope, settings = settings) }

            items(playbackVolumeGroups) {
                SettingsCheckboxListItem(
                    coroutineScope = coroutineScope,
                    settings = settings,
                    item = it
                )
            }

            item {
                SettingsMenuLink(
                    title = { Text("Equalizer") },
                    subtitle = { Text("Adjust the audio equalization to personalize the sound.") },
                    onClick = { /*TODO : Navigate to equalizer screen || navController.navigate()*/ }
                )
            }
        }
    }
)

@Composable
@NonRestartableComposable
private fun DataSaverGroup(coroutineScope : CoroutineScope,settings: Settings) = SettingsGroup(
    title = { Text("Data saver") },
    content = {
        dataSaverGroupItems(settings).forEach {
            SettingsCheckboxListItem(
                coroutineScope = coroutineScope,
                settings = settings,
                item = it
            )
        }
    }
)

private fun dataSaverGroupItems(settings: Settings) = listOf(
    ("Mobile data streaming" to "Allow streaming over cellular data.") to (settings.streamingOverMobileData to Settings.MOBILE_DATA_STREAMING),
    ("Download over mobile data" to "Download of music files when using cellular data.") to (settings.downloadOverMobileData to Settings.MOBILE_DATA_DOWNLOAD)
)

private fun playbackVolumeGroups(settings: Settings) = listOf(
    ("Gapless playback" to "Enable or disable gapless playback to eliminate pauses between tracks") to (settings.gaplessPlayback to Settings.GAPLESS_PLAYBACK),
    ("Normalize volume" to " Automatically adjust the volume of individual tracks to maintain a consistent playback level.") to (settings.normalizeVolume to Settings.NORMALIZE_VOLUME)
)
