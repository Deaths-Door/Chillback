package com.deathsdoor.chillback.ui.components.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.Preferences
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.deathsdoor.chillback.data.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SettingsCheckboxListItem(
    coroutineScope : CoroutineScope,
    settings : Settings,
    item : Pair<Pair<String, String>, Pair<Flow<Boolean>, Preferences.Key<Boolean>>>
) {
    val (title,description) = item.first
    val (_flow,key) = item.second
    val flow by _flow.collectAsState(false)
    val state = rememberBooleanSettingState(flow)

    SettingsCheckbox(
        state = state,
        title = { Text(title) },
        subtitle = { Text(description) },
        onCheckedChange = {
            coroutineScope.launch {
                settings.update(key,it)
            }
        }
    )
}