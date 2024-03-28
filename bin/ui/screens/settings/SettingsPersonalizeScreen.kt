package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.components.settings.SettingsCheckboxListItem
import com.deathsdoor.chillback.ui.components.settings.SettingsCollapsingToolbar
import com.deathsdoor.chillback.ui.components.settings.SettingsLanguageSelector
import com.deathsdoor.chillback.ui.components.settings.SettingsSleepTimerPicker
import com.deathsdoor.chillback.ui.providers.LocalSettings
import kotlinx.coroutines.launch

@Composable
fun SettingsPersonalizeScreen(navController: NavController) = SettingsCollapsingToolbar(
    navController = navController,
    text = SettingScreenRoutes.Personalize.title,
    body = {
        val settings = LocalSettings.current
        val coroutineScope = rememberCoroutineScope()

        val checkboxes = checkboxSettings(settings)

        // TODO : Add   - Storage used by app: View the amount of storage space used by the app.
        //      - Remove all downloads: Delete all downloaded music files.

        LazyColumn {
            item { SettingsLanguageSelector(settings = settings) }

            items(checkboxes) {
                SettingsCheckboxListItem(
                    coroutineScope = coroutineScope,
                    settings = settings,
                    item = it
                )
            }

            item { SettingsSleepTimerPicker(settings = settings) }
        }
    }
)

private fun checkboxSettings(settings: Settings) = listOf(
    ("Auto-load lyrics" to "Automatically load lyrics for songs when available") to (settings.autoLoadLyrics to Settings.AUTO_LOAD_LYRICS),
    ("Auto-load album art" to "Automatically load album art for songs when available") to (settings.autoLoadAlbumArt to Settings.AUTO_LOAD_ALBUM_ART),
    ("Auto-load all metadata" to "Automatically load all metadata for songs, such as artist, album, and track number") to (settings.autoLoadMetadata to Settings.AUTO_LOAD_METADATA),
    ("Autoplay when headset connected" to "Start playback automatically when a headset is connected.") to (settings.autoplayOnHeadsetConnection to Settings.AUTO_PLAY_HEADSET_CONNECT)
)