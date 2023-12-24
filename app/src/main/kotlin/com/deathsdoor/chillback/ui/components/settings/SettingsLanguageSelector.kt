package com.deathsdoor.chillback.ui.components.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsListDropdown
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.providers.LocalSettings

@Composable
fun SettingsLanguageSelector(modifier: Modifier = Modifier,settings: Settings) {

    val locales = settings.appLocales
    val languages = (0 until settings.appLocales.size()).map { locales[it].displayName }
    val state = rememberIntSettingState(languages.indexOf(settings.currentLocale.displayName))

    SettingsListDropdown(
        modifier = modifier,
        state = state,
        title = { Text(text = "Languages") },
        subtitle = { Text(text = "Change the app's language to your preferred language") },
        items = languages
    )
}