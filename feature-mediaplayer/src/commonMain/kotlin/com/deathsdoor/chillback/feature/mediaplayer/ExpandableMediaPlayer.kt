package com.deathsdoor.chillback.feature.mediaplayer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.ui.AstroPlayerState
import com.deathsdoor.chillback.feature.mediaplayer.components.CollapsedMediaPlayer
import com.deathsdoor.chillback.feature.mediaplayer.components.ExpandedMediaPlayer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableMediaPlayer(state: AstroPlayerState,modifier : Modifier = Modifier) = MusicPlayerShared(state) {
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    CollapsedMediaPlayer(
        modifier = modifier.padding(8.dp),
        state = state,
        onClick = {
            isSheetOpen = true
            coroutineScope.launch {
                sheetState.show()
            }
        }
    )

    if(!isSheetOpen) return@MusicPlayerShared

    val onDismiss = {
        isSheetOpen = false
        coroutineScope.launch {
            sheetState.hide()
        };
        Unit
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = null,
        content = {
            ExpandedMediaPlayer(
                state = state,
                onDismiss = onDismiss
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    )
}