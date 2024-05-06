package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsSlider
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.data.permissions.Permissions
import com.deathsdoor.chillback.data.permissions.Permissions.rememberReadExternalStoragePermissionState
import com.deathsdoor.chillback.data.settings.Settings
import com.deathsdoor.chillback.ui.components.permissions.ShowPermissionRationale
import com.deathsdoor.chillback.ui.components.settings.SettingsCheckboxListItem
import com.deathsdoor.chillback.ui.components.settings.SettingsCollapsingToolbar
import com.deathsdoor.chillback.ui.providers.LocalInfoSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsLibraryScreen(navController: NavController) = SettingsCollapsingToolbar(
    navController = navController,
    text = SettingScreenRoutes.Library.title,
    body = {
        val settings = LocalSettings.current
        val coroutineScope = rememberCoroutineScope()

        val enableLocalLibrarySettings by settings.showLocalLibrary.collectAsState(false)

        LazyColumn {
            // TODO :  get songs from server eg google drive / onedrive/ dropbox and have its settings here
            // TODO : Add media files / dirs that are blacklists in here
            item {
                val permissionState = rememberReadExternalStoragePermissionState()
                val context = LocalContext.current
                val infoSnackbarHostState = LocalInfoSnackbarState.current

                SettingsCheckboxListItem(
                    coroutineScope = coroutineScope,
                    settings = settings,
                    item = showLocalLibrary(settings),
                    onCheckChange = {
                        Permissions.managePermissionFlow(
                            permissionState = permissionState,
                            coroutineScope = coroutineScope,
                            context = context,
                            infoSnackbarHostState = infoSnackbarHostState
                        )
                    },
                    shouldUpdateValue = { permissionState.status.isGranted }
                )

                ShowPermissionRationale(permissionState)
            }

            item {
                val flow by settings.filterLocalLibrarySongsByDuration.collectAsState(30)
                val state = rememberIntSettingState(flow)

                SettingsSlider(
                    state = state,
                    valueRange = 10f..60f,
                    enabled = enableLocalLibrarySettings,
                    title = { Text("Filter Songs by Duration") },
                    onValueChangeFinished = {
                        coroutineScope.launch {
                            settings.update(Settings.FILTER_BY_DURATION,state.value)
                        }
                    }
                )
            }
        }
    }
)

private fun showLocalLibrary(settings: Settings) =
    ("Show Local Music Files" to "Display and play music files stored locally on the device.") to
            (settings.showLocalLibrary to Settings.SHOW_LOCAL_LIBRARY)
