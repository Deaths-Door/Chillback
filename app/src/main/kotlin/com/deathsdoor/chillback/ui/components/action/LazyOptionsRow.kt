package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberIsSingleItemRow() = remember { mutableStateOf(true) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LazyOptionsRow(
    coroutineScope : CoroutineScope,
    isSingleItemPerRow : MutableState<Boolean>,
    selectedIDs : MutableState<Set<Long>?>,
    data : Collection<T>?,
    criteria : Collection<String>,
    fetch : () -> Boolean,
    onSort : (isAscendingOrder : Boolean,appliedMethods : List<Int>) -> Unit,
    onFetch : suspend () -> Unit,
    key : (T) -> Long,
) = Row(modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()) {

    val optionState= rememberModalOptionsState(coroutineScope = coroutineScope)

    val enabled = remember(data) { mutableStateOf(!data.isNullOrEmpty()) }

    SortFilterButton(
        coroutineScope = coroutineScope,
        state = optionState,
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
            selectedIDs.value = when {
                // Enter Selection Mode
                selectedIDs.value == null -> setOf()

                // Since the values in the list are always unique , hence
                // Deselect All
                selectedIDs.value!!.size == data!!.size -> null

                // Select All
                selectedIDs.value!!.isNotEmpty() -> data.map { key(it) }.toSet()

                // Exit Selection Mode
                else -> null
            }
        },
        // TODO : Fix Icons
        content = { SelectedIcon(isSelected = selectedIDs.value?.isEmpty() ?: false) }
    )

    LazyLayoutChangeButton(isSingleItemPerRow = isSingleItemPerRow,enabled = enabled.value)
}