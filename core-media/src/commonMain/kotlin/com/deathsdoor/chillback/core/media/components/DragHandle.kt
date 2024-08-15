package com.deathsdoor.chillback.core.media.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableScope

@Composable
internal fun ReorderableCollectionItemScope.DraggableHandle(modifier : Modifier = Modifier,interactionSource : MutableInteractionSource) = Icon(
    modifier = modifier.draggableHandle(interactionSource = interactionSource),
    imageVector = Icons.Rounded.Menu,
    contentDescription = null
)