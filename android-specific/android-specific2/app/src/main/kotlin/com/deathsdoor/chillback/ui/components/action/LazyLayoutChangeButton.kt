package com.deathsdoor.chillback.ui.components.action

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize

@Composable
fun LazyLayoutChangeButton(
    isSingleItemPerRow : MutableState<Boolean>,
    enabled : Boolean
) {
    val windowAdaptiveInfo = LocalWindowAdaptiveSize.current

    when(windowAdaptiveInfo.widthSizeClass == WindowWidthSizeClass.Expanded &&  windowAdaptiveInfo.heightSizeClass != WindowHeightSizeClass.Compact) {
        true -> Box {
            var isMenuShown by remember { mutableStateOf(false) }
            _IconButton(isSingleItemPerRow, enabled) { isMenuShown = !isMenuShown }

            DropdownMenu(
                expanded = isMenuShown,
                onDismissRequest = { isMenuShown = false },
                content = {
                    Text(
                        text = "Change layout to",
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DropdownMenuItem(
                        text = { Text("Grid") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.grid_layout),
                                contentDescription = "Change layout to grid"
                            )
                        },
                        onClick = { isSingleItemPerRow.value = false }
                    )

                    DropdownMenuItem(
                        text = { Text("Vertical List") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.vertical_list_layout),
                                contentDescription = "Change layout to vertical List"
                            )
                        },
                        onClick = { isSingleItemPerRow.value = true }
                    )
                }
            )
        }
        false -> _IconButton(isSingleItemPerRow, enabled) { isSingleItemPerRow.value = !isSingleItemPerRow.value }
    }
}


@SuppressLint("ComposableNaming")
@Composable
@NonRestartableComposable
private fun _IconButton(
    isSingleItemPerRow : MutableState<Boolean>,
    enabled : Boolean,
    onClick : () -> Unit,
) = IconButton(
    enabled = enabled,
    onClick = onClick,
    content = {
        Icon(
            painter = painterResource(id = if(isSingleItemPerRow.value) R.drawable.vertical_list_layout else R.drawable.grid_layout),
            contentDescription = "Change List Layout (between grid and vertical"
        )
    }
)