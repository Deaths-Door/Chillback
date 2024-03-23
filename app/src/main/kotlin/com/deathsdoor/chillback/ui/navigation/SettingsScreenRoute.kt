package com.deathsdoor.chillback.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.R

private const val MAIN = "settings"
private const val GROUPED = "grouped"
private const val THEME = "theme"
private const val WIDGET = "widget"
private const val AUDIO = "audio"
private const val PERSONALIZE = "personalize"
private const val ONDEVICELIBRARY = "ondevicelibrary"
private const val BACKUPRESTORE = "backuprestore"
private const val ABOUT = "about"

fun NavGraphBuilder.addSettingsScreenRoutes() = navigation(route = MAIN, startDestination = GROUPED) {
   composable(route = GROUPED) { TODO() }
   composable(route = THEME) { TODO() }
   composable(route = WIDGET) { TODO() }
   composable(route = AUDIO) { TODO() }
   composable(route = PERSONALIZE) { TODO() }
   composable(route = ONDEVICELIBRARY) { TODO() }
   composable(route = BACKUPRESTORE) { TODO() }
   composable(route = ABOUT) { TODO() }
}

// TODO : Update Icons
fun forEachCategory(on : (id : String,icon : ImageVector,title : Int,description : Int) -> Unit) {
   on(THEME,Icons.Default.Settings,R.string.settings_theme_title,R.string.settings_theme_description)
   on(WIDGET,Icons.Default.Settings,R.string.settings_widget_title,R.string.settings_widget_description)
   on(AUDIO,Icons.Default.Settings,R.string.settings_audio_title,R.string.settings_audio_description)
   on(PERSONALIZE,Icons.Default.Settings,R.string.settings_personalize_title,R.string.settings_personalize_description)
   on(ONDEVICELIBRARY,Icons.Default.Settings,R.string.settings_ondevicelibrary_title,R.string.settings_ondevicelibrary_description)
   on(BACKUPRESTORE,Icons.Default.Settings,R.string.settings_backuprestore_title,R.string.settings_backuprestore_description)
   on(ABOUT,Icons.Default.Settings,R.string.settings_about_title,R.string.settings_about_description)
}

fun NavController.navigateToSettings() = navigate(MAIN)