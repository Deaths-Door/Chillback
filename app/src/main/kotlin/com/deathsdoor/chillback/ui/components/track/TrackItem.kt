package com.deathsdoor.chillback.ui.components.track

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.ui.components.layout.AdaptiveGridThumbnail
import com.deathsdoor.chillback.ui.components.mediaplayer.LikeButton

@Composable
fun TrackItem(
    modifier : Modifier = Modifier,
    track : Track,
    details : TrackDetails?,
    isSingleItemPerRow : Boolean,
    isSelected : Boolean?,
    selectedIDs : MutableState<Set<Long>?>
) {
    val artists =  details?.artistWithOthers()
    val name = details?.name ?: "Wait a second"

    AdaptiveGridThumbnail(
        modifier = modifier,
        title = name,
        subtitle = artists,
        uri = details?.artwork,
        isSelected = isSelected,
        isSingleItemPerRow = isSingleItemPerRow,
        selectedIds = selectedIDs,
        id = { track.id },
        actionIcon = { actionModifier , enabled ->
            LikeButton(
                modifier = actionModifier,
                enabled = enabled,
                track = track,
            )
        }
    )
}