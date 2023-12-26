package com.deathsdoor.chillback.ui.components.layout.modalsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Meant as a utils wrapper to reduce boilerplate and allow more complex uses of bottomsheet
@OptIn(ExperimentalMaterial3Api::class)
class ModalSheetState constructor(
    internal val coroutineScope: CoroutineScope,
    internal val sheetState : SheetState
) {
    internal var sheetShown by mutableStateOf(sheetState.isVisible)
        private set

    fun showSheet() {
        sheetShown = true
        coroutineScope.launch { sheetState.show() }
    }

    fun dismissSheet() {
        sheetShown = false
        coroutineScope.launch { sheetState.hide() }
    }

    companion object {
        internal fun Saver(
            skipPartiallyExpanded: Boolean,
            coroutineScope: CoroutineScope,
            confirmValueChange: (SheetValue) -> Boolean
        ) = Saver<ModalSheetState, SheetValue>(
            save = { it.sheetState.currentValue },
            restore = { savedValue ->
                val sheetState = SheetState(skipPartiallyExpanded, savedValue, confirmValueChange)

                ModalSheetState(
                    coroutineScope = coroutineScope,
                    sheetState = sheetState
                )
            }
        )
    }
}