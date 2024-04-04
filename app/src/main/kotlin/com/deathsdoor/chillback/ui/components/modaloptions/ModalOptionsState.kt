package com.deathsdoor.chillback.ui.components.modaloptions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Deprecated("Replace this with modal bottomsheet maybe")
@OptIn(ExperimentalMaterial3Api::class)
class ModalOptionsState constructor(
    private val coroutineScope: CoroutineScope,
    internal val sheetState : SheetState
) {
    var isShown by mutableStateOf(sheetState.isVisible)
        private set

    fun show() {
        isShown = true
        coroutineScope.launch { sheetState.show() }
    }

    fun dismiss() {
        isShown = false
        coroutineScope.launch { sheetState.hide() }
    }
}