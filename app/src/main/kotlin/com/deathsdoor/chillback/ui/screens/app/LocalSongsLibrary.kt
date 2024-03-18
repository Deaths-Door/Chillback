package com.deathsdoor.chillback.ui.screens.app

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.repositories.LocalSongsRepository
import com.deathsdoor.chillback.ui.components.action.BackButton
import com.deathsdoor.chillback.ui.components.collection.LazyTrackCollectionList
import com.deathsdoor.chillback.ui.components.layout.RequestPermissionsAndThen
import com.deathsdoor.chillback.ui.components.layout.SlideFromUnderneathText
import com.deathsdoor.chillback.ui.components.track.LazyTrackList
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocalSongsLibraryScreen() {
    val permissionState= rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val appState= LocalAppState.current
    val navController = appState.navController

    // TODO : Improve this UI by https://user-images.githubusercontent.com/94031495/181867586-5d661af4-03f2-4911-9fb1-22141d7e69fc.png
    RequestPermissionsAndThen(
        permissionState = permissionState,
        onDismiss = { navController.popBackStack() },
        permissionRequiredReason = { "To show your local music library, this screen needs permission to access your device's storage. Granting permission lets you enjoy your music without limitations." }
    ) {
        val repository = appState.userRepository.localTracks
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            repository.applyCoroutineScope(coroutineScope)
        }

        var currentItem by remember { mutableIntStateOf(0) }
        val tracks by repository.tracks.collectAsState()

        Scaffold(
           // modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    LocalSongsLibraryScreenNavigationBarContents(enabled = !tracks.isNullOrEmpty()) { currentItem = it }
                }
            },
            topBar = {
                LocalSongsLibraryScreenTopAppBar(
                    navController = navController,
                    currentItem = currentItem
                )
            },
            content = { paddingValues ->
                val paddingModifier = Modifier.padding(paddingValues)

                if (currentItem == 0) {
                    // TODO : Add playall and shufle play options to the root list
                    LazyTrackList(
                        modifier = paddingModifier,
                        coroutineScope = coroutineScope,
                        tracks = tracks,
                        placeHolderText = {
                            // TODO: Add an icon to navigate to the settings that need to be changed for it to work
                            styledText(
                                plain0 = "Your local music library is currently empty.\n",
                                colored0 = "To see your favorite music here, add songs to your device",
                                plain1 = " or ",
                                colored1 = "change your local music filter settings."
                            )
                        },
                        onRemove = null,
                        onTracksSorted = {},
                    )

                    return@Scaffold
                }

                var collections: List<TrackCollection> by remember { mutableStateOf(emptyList()) }

                LaunchedEffect(currentItem) {
                    collections = repository.sorted(currentItem)
                }

                LazyTrackCollectionList(
                    modifier = paddingModifier,
                    collections = collections,
                    placeHolderText = {
                        styledText(
                            plain0 = "Hold on a beat...\nWe're ",
                            colored0 = "sorting your music library",
                            plain1 = "",
                            colored1 = ""
                        )
                    },
                    coroutineScope = coroutineScope,
                    onPinChange = { throw IllegalStateException("Should be unreachable") },
                    onDelete = { throw IllegalStateException("Should be unreachable") }
                )
            }
        )
    }
}

// TODO : ADD SEARCH ITEM THINGY
@Composable
private fun RowScope.LocalSongsLibraryScreenNavigationBarContents(enabled : Boolean,onClick : (index : Int) -> Unit) {
    LocalSongsRepository.items.asIterable().forEachIndexed { index, (label,id) ->
        NavigationBarItem(
            selected = false,
            alwaysShowLabel = false,
            enabled = enabled,
            icon = { Icon(painter = painterResource(id = id), contentDescription = null) },
            label = { Text(label) },
            onClick = { onClick(index) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalSongsLibraryScreenTopAppBar(navController : NavController,currentItem : Int) = TopAppBar(
    navigationIcon = { BackButton { navController.popBackStack() } },
    title = {
        AnimatedContent(currentItem) {
            SlideFromUnderneathText(
                text = LocalSongsRepository.items.keys.elementAt(it)
            )
        }
    }
)