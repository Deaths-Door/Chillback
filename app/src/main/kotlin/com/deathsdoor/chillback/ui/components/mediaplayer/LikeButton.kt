package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    isLiked : Boolean,
    enabled : Boolean = true,
    onValueChange : (Boolean) -> Unit,
) = IconToggleButton(
    modifier = modifier,
    enabled = enabled,
    checked = isLiked,
    onCheckedChange = onValueChange,
    content = {
        if (isLiked) Icon(
            imageVector = Icons.Filled.Favorite,
            tint = MaterialTheme.colorScheme.onError,
            contentDescription = "Current Song is Liked",
        )
        else Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Current Song is disliked",
        )
    }
)