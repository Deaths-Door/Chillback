package com.deathsdoor.chillback.ui.components.action

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R

@Composable
@NonRestartableComposable
fun LazyLayoutChangeButton(
    isSingleItemPerRow : MutableState<Boolean>,
    enabled : Boolean
) = IconButton(
    enabled = enabled,
    onClick = { isSingleItemPerRow.value = !isSingleItemPerRow.value },
    content = {
        Icon(
            painter = painterResource(id = if(isSingleItemPerRow.value) R.drawable.vertical_list_layout else R.drawable.grid_layout),
            contentDescription = "Change List Layout (between grid and vertical"
        )
    }
)