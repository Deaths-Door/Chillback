package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Deprecated("Do not use")
@Composable
fun MoreInfoButton(modifier: Modifier = Modifier, onClick : () -> Unit,s: Int = 0) = IconButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options"
        )
    }
)

@Composable
fun MoreInfoButton(
    modifier: Modifier = Modifier,
    content : @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { expanded = true },
            content = {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options"
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = content
        )
    }
}