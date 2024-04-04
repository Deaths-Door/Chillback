package com.deathsdoor.chillback.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.deathsdoor.chillback.ui.components.auth.UserProfilePhoto
import com.deathsdoor.chillback.ui.components.auth.UserProfilePhotoWithDropDown
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.mediaplayer.screen.MusicPlayerBar
import com.deathsdoor.chillback.ui.components.mediaplayer.screen.MusicPlayerExpandableCard
import com.deathsdoor.chillback.ui.components.navigationsuite.ScaffoldSuiteScaffold
import com.deathsdoor.chillback.ui.extensions.applyOnNotNull
import com.deathsdoor.chillback.ui.navigation.LocalSongsLibrary
import com.deathsdoor.chillback.ui.navigation.addAppScreenRoutes
import com.deathsdoor.chillback.ui.navigation.addMusicScreenRoutes
import com.deathsdoor.chillback.ui.navigation.addSettingsScreenRoutes
import com.deathsdoor.chillback.ui.navigation.addTrackExtraOptionsRoutes
import com.deathsdoor.chillback.ui.navigation.appScreenInitialRoute
import com.deathsdoor.chillback.ui.navigation.forEachCoreAppScreenRoute
import com.deathsdoor.chillback.ui.navigation.navigateToSettings
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize

@Composable
fun ChillbackApp() {
    val appState = LocalAppState.current
    val navController =  appState.navController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val uiContent = @Composable { content : @Composable (PaddingValues?) -> Unit ->
        ScaffoldSuiteScaffold(
            scaffoldSuiteItems = {
                header = { isDesktop ->
                    val modifier = Modifier.padding(vertical = 32.dp)
                    when(isDesktop) {
                        true -> UserProfilePhotoWithDropDown(modifier = modifier.padding(horizontal = 32.dp))
                        false -> UserProfilePhoto(modifier = modifier.clickable(
                            role = Role.Button,
                            onClickLabel = "Navigate to your profile",
                            onClick = { navController.navigateToSettings() }
                        ))
                    }
                }

                // TODO : Show playlists in this side nav

                footer = { isDesktop ->
                    val modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable(
                            onClickLabel = "Go To Settings",
                            onClick = { navController.navigateToSettings() }
                        )

                    when(isDesktop) {
                        true -> Thumbnail(
                            modifier = modifier.padding(horizontal = 16.dp),
                            title = "Settings",
                            artwork = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                )
                            }
                        )
                        false -> Icon(
                            modifier = modifier,
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    }
                }

                forEachCoreAppScreenRoute { route, label, id ->
                    navigationItem(
                        selected = currentRoute == route,
                        icon = { Icon(painter = painterResource(id = id), contentDescription = null) },
                        label = { Text(label) },
                        onClick = {
                            navController.navigate(route) {
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            },
            content = content
        )
    }

    val windowAdaptiveSize = LocalWindowAdaptiveSize.current

    val navHost = @Composable { modifier : Modifier ->
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = appScreenInitialRoute,
            builder = {
                addAppScreenRoutes()
                addSettingsScreenRoutes(windowAdaptiveSize)
                addMusicScreenRoutes(appState = appState)
                addTrackExtraOptionsRoutes()
            }
        )
    }


    // TODO : Check if this condition is correct
    // TODO : For desktop/large screens , make a mini-playbackqueue that doesnt take up full screen with pane
    when(windowAdaptiveSize.widthSizeClass != WindowWidthSizeClass.Compact && windowAdaptiveSize.heightSizeClass != WindowHeightSizeClass.Compact) {
        // desktop
        true -> uiContent { paddingValues ->
            Column(modifier = Modifier
                .applyOnNotNull(paddingValues) { padding(it) }
                .fillMaxSize()
            ) {
                navHost(Modifier.weight(1f))
                MusicPlayerBar(modifier = Modifier.padding(vertical = 16.dp))
            }
        }
        false -> uiContent { paddingValues ->
            Box(
                modifier = Modifier
                    .applyOnNotNull(paddingValues) { padding(it) }
                    .fillMaxSize(),
                content = {
                    navHost(Modifier.matchParentSize())

                    MusicPlayerExpandableCard(modifier = Modifier
                        // Because of NavigationBarTokens.ContainerHeight = 80.dp
                        .padding(bottom = if (currentRoute != LocalSongsLibrary) 0.dp else 80.dp)
                        .align(Alignment.BottomCenter)
                    )
                }
            )
        }
    }
}


@Composable
@Preview
@PreviewFontScale
@PreviewScreenSizes
internal fun ChillbackAppPreview() {
    InitializeProvidersForPreview {
        ChillbackApp()
    }
}