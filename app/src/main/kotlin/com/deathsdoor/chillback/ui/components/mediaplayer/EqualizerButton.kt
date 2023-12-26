package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.modalsheet.ModalSheet
import com.deathsdoor.chillback.ui.components.layout.modalsheet.rememberModalSheetState
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerButton(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope
) {
    val sheetState = rememberModalSheetState(
        coroutineScope = coroutineScope,
        skipPartiallyExpanded = true
    )

    IconButton(
        modifier = modifier,
        onClick = { sheetState.showSheet() },
        content = {
            Icon(
                painter = painterResource(R.drawable.ic_equalizer),
                contentDescription = "Open Equalizer Settings"
            )
        }
    )

    ModalSheet(sheetState, dragHandle = null) {
        // TODO : Create Equalizer Screen
        Text("text",modifier = Modifier.fillMaxSize().background(Color.Red))
    }
}