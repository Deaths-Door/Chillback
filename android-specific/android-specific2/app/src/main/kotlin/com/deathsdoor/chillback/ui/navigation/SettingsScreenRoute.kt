package com.deathsdoor.chillback.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.screens.settings.SettingsAudioPortraitMobile
import com.deathsdoor.chillback.ui.screens.settings.SettingsOverviewPortraitMobile

private const val MAIN = "settings"
private const val OVERVIEW = "overview"
private const val THEME = "theme"
private const val WIDGET = "widget"
private const val AUDIO = "audio"
private const val PERSONALIZE = "personalize"
private const val ONDEVICELIBRARY = "ondevicelibrary"
private const val BACKUPRESTORE = "backuprestore"
private const val ABOUT = "about"

fun NavGraphBuilder.addSettingsScreenRoutes(windowSizeClass: WindowSizeClass) {
   when(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
           && windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact
   ) {
      // desktop / large screen
      true -> composable(route = MAIN) { TODO() }
      false -> navigation(route = MAIN, startDestination = OVERVIEW) {
         composable(route = OVERVIEW) { SettingsOverviewPortraitMobile() }
         composable(route = THEME) { TODO() }
         composable(route = WIDGET) { TODO() }
         composable(route = AUDIO) { SettingsAudioPortraitMobile() }
         composable(route = PERSONALIZE) { TODO() }
         composable(route = ONDEVICELIBRARY) { TODO() }
         composable(route = BACKUPRESTORE) { TODO() }
         composable(route = ABOUT) { TODO() }
      }
   }
}

private typealias Closure = (route : String,icon : ImageVector,label : Int,description : Int) -> Unit

@Composable
fun WidgetThemeCategory(on: @Composable Closure) {
   on(THEME,Icons.Default.Settings,R.string.settings_theme_title,R.string.settings_theme_description)
   on(WIDGET,Icons.Default.Settings,R.string.settings_widget_title,R.string.settings_widget_description)
}

@Composable
fun OtherCategory(on: @Composable Closure) {
   on(AUDIO,Icons.Default.Settings,R.string.settings_audio_title,R.string.settings_audio_description)
   on(PERSONALIZE,Icons.Default.Settings,R.string.settings_personalize_title,R.string.settings_personalize_description)
   on(ONDEVICELIBRARY,Icons.Default.Settings,R.string.settings_ondevicelibrary_title,R.string.settings_ondevicelibrary_description)
   on(BACKUPRESTORE,Icons.Default.Settings,R.string.settings_backuprestore_title,R.string.settings_backuprestore_description)
   on(ABOUT,Icons.Default.Settings,R.string.settings_about_title,R.string.settings_about_description)
}

// TODO : Update Icons
@Deprecated("Do not use")
fun forEachCategory(on : Closure) {
   on(THEME,Icons.Default.Settings,R.string.settings_theme_title,R.string.settings_theme_description)
   on(WIDGET,Icons.Default.Settings,R.string.settings_widget_title,R.string.settings_widget_description)
   on(AUDIO,Icons.Default.Settings,R.string.settings_audio_title,R.string.settings_audio_description)
   on(PERSONALIZE,Icons.Default.Settings,R.string.settings_personalize_title,R.string.settings_personalize_description)
   on(ONDEVICELIBRARY,Icons.Default.Settings,R.string.settings_ondevicelibrary_title,R.string.settings_ondevicelibrary_description)
   on(BACKUPRESTORE,Icons.Default.Settings,R.string.settings_backuprestore_title,R.string.settings_backuprestore_description)
   on(ABOUT,Icons.Default.Settings,R.string.settings_about_title,R.string.settings_about_description)
}

fun NavController.navigateToSettings() = navigate(MAIN)