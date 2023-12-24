package com.deathsdoor.chillback.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.deathsdoor.chillback.ui.screens.settings.AboutSectionScreen
import com.deathsdoor.chillback.ui.screens.settings.SettingsAudioScreen
import com.deathsdoor.chillback.ui.screens.settings.SettingsGroupedScreen
import com.deathsdoor.chillback.ui.screens.settings.SettingsPersonalizeScreen

private const val _ROUTE : String = "settings"

enum class SettingScreenRoutes(override val route: String) : Route {
    SettingGrouped("$_ROUTE/grouped") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Settings"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = throw UnsupportedOperationException()

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = SettingsGroupedScreen(navController)
    },
    ThemeSelector("$_ROUTE/theme") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Themes"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = throw UnsupportedOperationException()

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = TODO()

    },
    WidgetSelector("$_ROUTE/widget") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Widgets"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = throw UnsupportedOperationException()

        @Composable
        override fun Content(navController: NavHostController) = TODO()
    },
    Audio("$_ROUTE/audio") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Audio"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = "Customize your music playback experience."

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = SettingsAudioScreen(navController)
    },
    Personalize("$_ROUTE/personalize") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Personalize"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = "Tailor the app to your preferences."

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = SettingsPersonalizeScreen(navController)
    },
    Library("$_ROUTE/lib") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Local Library"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = "Enhance your music library management."

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = TODO()
    },
    BackupRestore("$_ROUTE/backuprestore") {
        override val title: String
            @ReadOnlyComposable
            @Composable
            get() = "Backup & Restore"

        override val description: String
            @ReadOnlyComposable
            @Composable
            get() = "Backup and restore your settings, your songs and your playlists"

        @Composable
        override fun Content(navController: NavHostController) = TODO()
    },
    About("$_ROUTE/about") {
        override val title: String
        @ReadOnlyComposable
        @Composable
        get() = "About"

        override val description: String
        @ReadOnlyComposable
        @Composable
        get() = "Learn about the app and its creator"

        @Composable
        @NonRestartableComposable
        override fun Content(navController: NavHostController) = AboutSectionScreen(navController)
    };

    abstract val title : String
        @ReadOnlyComposable
        @Composable
        get

    // Not there for SettingsGrouped , Theme / Widget selector
    abstract val description : String
        @ReadOnlyComposable
        @Composable
        get

    companion object {
        const val ROUTE = _ROUTE

        fun navGraph(navGraph: NavGraphBuilder, navController : NavHostController) = navGraph.navigation(
            startDestination = SettingGrouped.route,
            route = ROUTE,
            builder = {
                SettingScreenRoutes.values().forEach { route ->
                    composable(route = route.route, content = { route.Content(navController) })
                }
            }
        )
    }
}