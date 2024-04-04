package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.runtime.Composable

// TODO
@Composable
fun SettingsAudioPortraitMobile() = Unit //{
    /*val appState= LocalAppState.current
    CollapsableScaffold(
        label = stringResource(id = R.string.settings_audio_title),
        headerContent = { modifier -> /**TODO() **/ },
        onBack = { appState.navController.popBackStack() },
        content = {
            val settings = appState.settings
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
}

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
*/