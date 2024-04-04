package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dragselectcompose.core.DragSelectState
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberIsSingleItemRow() = remember { mutableStateOf(true) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ColumnScope.LazyOptionsRow(
    coroutineScope: CoroutineScope,
    isSingleItemPerRow: MutableState<Boolean>,
    draggableState: DragSelectState<T>,
    data: List<T>?,
    criteria: Collection<String>,
    fetch: () -> Boolean,
    onSort: (isAscendingOrder: Boolean, appliedMethods: List<Int>) -> Unit,
    onFetch: suspend () -> Unit,
) {
    @Suppress("UNUSED_EXPRESSION")
    this

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        val enabled = remember(data) { mutableStateOf(!data.isNullOrEmpty()) }

        SortFilterButton(
            coroutineScope = coroutineScope,
            enabled = enabled,
            criteria = criteria,
            fetch = fetch,
            onFetch = onFetch,
            onSort = onSort
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            enabled = enabled.value,
            onClick = {
                when {
                    // Enter Selection Mode
                    !draggableState.inSelectionMode -> draggableState.enableSelectionMode()

                    // Since the values in the list are always unique , hence
                    // Deselect All
                    draggableState.selected.size == data!!.size -> draggableState.disableSelectionMode()

                    // Select All
                    draggableState.selected.isNotEmpty() -> draggableState.updateSelected(data)

                    // Exit Selection Mode
                    else -> draggableState.disableSelectionMode()
                }
            },
            content = {
                if(draggableState.inSelectionMode) SelectedIcon(isSelected = draggableState.selected.isEmpty())
            }
        )

        LazyLayoutChangeButton(isSingleItemPerRow = isSingleItemPerRow,enabled = enabled.value)
    }

    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
}