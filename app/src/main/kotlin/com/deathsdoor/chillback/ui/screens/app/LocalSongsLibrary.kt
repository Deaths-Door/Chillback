package com.deathsdoor.chillback.ui.screens.app

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissDirection
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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.action.BackButton
import com.deathsdoor.chillback.ui.components.layout.RequestPermissionsAndThen
import com.deathsdoor.chillback.ui.components.layout.SlideFromUnderneathText
import com.deathsdoor.chillback.ui.components.track.LazyTrackList
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocalSongsLibraryScreen() {
    val permissionState= rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val appState= LocalAppState.current
    val navController = appState.navController

    RequestPermissionsAndThen(
        permissionState = permissionState,
        onDismiss = { navController.popBackStack() },
        permissionRequiredReason = { "To show your local music library, this screen needs permission to access your device's storage. Granting permission lets you enjoy your music without limitations." },
        content = {
            val repository = appState.userRepository.localTracks
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(Unit){
                repository.applyCoroutineScope(coroutineScope)
            }

            val tracks by repository.tracks.collectAsState()

            var currentItem by remember { mutableIntStateOf(0) }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        LocalSongsLibraryScreenNavigationBarContents { currentItem = it }
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

                    AnimatedContent(currentItem) {
                        if(it == 0) {
                            LazyTrackList(
                                modifier = paddingModifier,
                                coroutineScope = coroutineScope,
                                directions = setOf(DismissDirection.EndToStart),
                                tracks = tracks,
                                onRemove = { _ , _ -> throw IllegalStateException("Should be unreachable") } ,
                                onTracksSorted = {}
                            )

                            return@AnimatedContent
                        }

                        //TODO : OTHER LISTS + SORT ITEMS ETC
                    }
                }
            )
        }
    )

    // back button || Title
    // Pager with song lists
    // Bottom app bar
}

// TODO : CHANGE ICONS
private val items by lazy {
    mapOf(
        "Songs" to R.drawable.add_to_queue,
        "Genre" to R.drawable.add_to_queue,
        "Album" to R.drawable.add_to_queue,
        "Artists" to R.drawable.add_to_queue,
    )
}

@Composable
private fun RowScope.LocalSongsLibraryScreenNavigationBarContents(onClick : (index : Int) -> Unit) {
    items.asIterable().forEachIndexed { index, (label,id) ->
        NavigationBarItem(
            selected = false,
            alwaysShowLabel = false,
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
                text = items.keys.elementAt(it)
            )
        }
    }
)