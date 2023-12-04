package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.media3.session.MediaController

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    mediaController : MediaController,
) {
    // TODO : Update the 'liked' field correctly
    var isLiked : Boolean? = null

    IconToggleButton(
        modifier = modifier,
        checked = isLiked ?: false,
        onCheckedChange = {
            isLiked = it
        },
        content = {
            if (isLiked == true) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Current Song is Liked",
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Current Song is disliked",
                )
            }
        }
    )
}