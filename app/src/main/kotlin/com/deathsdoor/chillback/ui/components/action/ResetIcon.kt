package com.deathsdoor.chillback.ui.components.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
@NonRestartableComposable
fun ResetIcon(state : MutableState<String?>, default : String) = ResetIcon { state.value = default }

@Composable
@NonRestartableComposable
fun ResetIcon(state : SnapshotStateList<String>, default : List<String>) = ResetIcon {
    state.clear()
    state.addAll(default)
}

@Composable
@NonRestartableComposable
private fun ResetIcon(onClick : () -> Unit) = IconButton(
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Field"
        )
    }
)