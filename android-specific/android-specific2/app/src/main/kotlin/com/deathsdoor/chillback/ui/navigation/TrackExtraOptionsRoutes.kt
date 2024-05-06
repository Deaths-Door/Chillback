package com.deathsdoor.chillback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.ui.components.track.TrackMetadataScreen

private const val TRACK_EXTRA_OPTIONS_ROUTE = "trackextraoptions"
private const val BASE = "base"
private const val TRACK_EXTRA_OPTIONS_METADATA = "metadata"
private const val SOURCE_PATH = "sourcePath"
private const val ID = "id"
//private const val PLAYBACKQUEUE = "playbackqueue"
const val TRACK_EXTRA_OPTIONS_METADATA_ROUTE = "$TRACK_EXTRA_OPTIONS_METADATA/{$ID}/{$SOURCE_PATH}"

fun NavGraphBuilder.addTrackExtraOptionsRoutes() {
    navigation(route = TRACK_EXTRA_OPTIONS_ROUTE, startDestination = BASE) {
        composable(BASE) {}
        composable(
            route = TRACK_EXTRA_OPTIONS_METADATA_ROUTE,
            arguments =  listOf(
                navArgument(ID) { type = NavType.LongType },
                navArgument(SOURCE_PATH) { type = NavType.StringType }
            ),
            content = { backStackEntry ->
                TrackMetadataScreen(
                    track = Track(
                        sourcePath = Track.decodeSourcePath(sourcePath = backStackEntry.arguments!!.getString(SOURCE_PATH)!!),
                    ).apply { id = backStackEntry.arguments!!.getLong(ID) }
                )
            }
        )

        /*if(
            !(windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Expanded &&
                    windowAdaptiveSize.heightSizeClass != WindowHeightSizeClass.Compact)
        ) composable(route = PLAYBACKQUEUE) { PlaybackQueuePortraitScreen() }*/
    }
}

fun NavController.navigateToTrackMetadataScreen(track : Track) {
    navigate(TRACK_EXTRA_OPTIONS_ROUTE)
    navigate("$TRACK_EXTRA_OPTIONS_METADATA/${track.id}/${track.encodeSourcePath()}")
}

/*
fun NavController.navigateToPlaybackScreen(track : Track) {
    navigate(TRACK_EXTRA_OPTIONS_ROUTE)
    navigate(PLAYBACKQUEUE)
}*/