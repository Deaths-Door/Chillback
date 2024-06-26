package com.deathsdoor.chillback.ui.components.action

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortFilterButton(
    coroutineScope: CoroutineScope,
    enabled : MutableState<Boolean>,
    criteria : Collection<String>,
    fetch : () -> Boolean,
    onFetch : suspend () -> Unit,
    onSort :  (isAscendingOrder : Boolean,appliedMethods : List<Int>) -> Unit,
) {
    val windowAdaptiveInfo = LocalWindowAdaptiveSize.current

    when(windowAdaptiveInfo.widthSizeClass == WindowWidthSizeClass.Expanded &&  windowAdaptiveInfo.heightSizeClass != WindowHeightSizeClass.Compact) {
        true -> Box {
            var isMenuShown by remember { mutableStateOf(false) }

            Row(modifier = Modifier.padding(end = 24.dp)) {
                _IconButton(
                    enabled = enabled.value,
                    onClick = {
                        isMenuShown = !isMenuShown
                    }
                )

                Text(
                    text = "Sort and Filter List",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            val onDismiss = { isMenuShown = false }

            DropdownMenu(
                expanded = isMenuShown,
                onDismissRequest = onDismiss,
                content = {
                    SortFilterContent(
                        onDismiss = onDismiss,
                        criteria = criteria,
                        coroutineScope = coroutineScope,
                        onSort = onSort,
                        onFetch = onFetch,
                        fetch = fetch,
                        // Fixes potential issue of sorting the list multiple times simultaneously
                        // By changing the [enabled] of the sorting button, so user can not sort it multiple times simultaneously
                        changeEnabled = { enabled.value = it }
                    )
                }
            )
        }
        false -> {
            var isSheetShown by remember { mutableStateOf(false) }
            val sheetState = rememberModalBottomSheetState()

            _IconButton(enabled = enabled.value) {
                isSheetShown = true
                coroutineScope.launch {
                    sheetState.show()
                }
            }

            val onDismiss = {
                isSheetShown = false
                coroutineScope.launch {
                    sheetState.hide()
                }
                Unit
            }

            if(!isSheetShown) return

            ModalBottomSheet(
                onDismissRequest = onDismiss,
                content = {
                    val modifier = Modifier.padding(horizontal = 12.dp)
                    val maxWidth = modifier.fillMaxWidth()

                    Text(
                        modifier = maxWidth,
                        text = "Sort and Filter",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )

                    SortFilterContent(
                        _modifier = modifier,
                        _maxWidth = maxWidth,
                        onDismiss = onDismiss,
                        criteria = criteria,
                        coroutineScope = coroutineScope,
                        onSort = onSort,
                        onFetch = onFetch,
                        fetch = fetch,
                        // Fixes potential issue of sorting the list multiple times simultaneously
                        // By changing the [enabled] of the sorting button, so user can not sort it multiple times simultaneously
                        changeEnabled = { enabled.value = it }
                    )
                }
            )
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
@NonRestartableComposable
private fun _IconButton(
    enabled : Boolean,
    onClick : () -> Unit,
) = IconButton(
    enabled = enabled,
    onClick = onClick,
    content = {
        Icon(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = "Sort and Filter List"
        )
    }
)

@Suppress("LocalVariableName")
@SuppressLint("ModifierParameter")
@OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)
@NonRestartableComposable
@Composable
private fun ColumnScope.SortFilterContent(
    onDismiss : () -> Unit,
    coroutineScope: CoroutineScope,
    criteria : Collection<String>,
    fetch : () -> Boolean,
    onFetch : suspend () -> Unit,
    onSort :  (isAscendingOrder : Boolean,appliedMethods : List<Int>) -> Unit,
    changeEnabled : (Boolean) -> Unit,
    // Look at the default values of the modifiers
    _modifier : Modifier = Modifier.padding(horizontal = 12.dp),
    _maxWidth : Modifier = _modifier.fillMaxWidth()
) {
    @Suppress("UNUSED_EXPRESSION") this

    Text(
        modifier = _modifier,
        text = "Order",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
    )

    var isAscendingOrder by remember { mutableStateOf(true) }
    val arrangement = Arrangement.spacedBy(8.dp)

    Row(modifier = _modifier, horizontalArrangement = arrangement) {
        FilterChip(
            selected = isAscendingOrder,
            onClick = { isAscendingOrder = true },
            label = { Text("Ascending") },
            leadingIcon = ifSelectedLeadingIcon(selected = isAscendingOrder, label = "Ascending")
        )

        FilterChip(
            selected = !isAscendingOrder,
            onClick = { isAscendingOrder = false },
            label = { Text("Descending") },
            leadingIcon = ifSelectedLeadingIcon(selected = !isAscendingOrder, label = "Descending")
        )
    }

    Text(
        modifier = _modifier,
        text = "By",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
    )

    val appliedMethods = remember { mutableStateListOf<Int>() }

    FlowRow(
        modifier = _modifier,
        horizontalArrangement = arrangement,
        verticalArrangement = arrangement
    ) {
        criteria.forEachIndexed { index, label ->
            val selected = appliedMethods.contains(index)
            FilterChip(
                selected = selected,
                onClick = {
                    if (selected) appliedMethods.removeAt(index) else appliedMethods.add(
                        index
                    )
                },
                label = { Text(label) },
                leadingIcon = ifSelectedLeadingIcon(selected = selected, label = label)
            )
        }
    }

    var isDialogOpen by remember { mutableStateOf(false) }

    Row(modifier = _maxWidth, horizontalArrangement = Arrangement.SpaceEvenly) {
        val isNotEmpty = appliedMethods.isNotEmpty()
        OutlinedButton(
            enabled = isNotEmpty || !isAscendingOrder,
            onClick = { isAscendingOrder = true;appliedMethods.clear() },
            content = {
                Text("Reset All")
                if (isNotEmpty) Badge(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        "${appliedMethods.size}"
                    )
                }
            }
        )

        Button(
            onClick = {
                if (fetch()) isDialogOpen = true
                else coroutineScope.launch { onSort(isAscendingOrder, appliedMethods) }
            },
            content = { Text("Apply") }
        )
    }

    if(!isDialogOpen) return

    var waitingForDetails by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { isDialogOpen = false },
        title = { Text("Fetch Details and then sort?") },
        text = {
            if(waitingForDetails) {
                Text(text = "Fetching details ...", fontWeight = FontWeight.Bold)
                LinearProgressIndicator(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())
                return@AlertDialog
            }

            Text("Some details are missing. Sort with available info now, or wait for all to load?")
        },
        dismissButton = {
            Button(
                onClick = {
                    coroutineScope.launch { onSort(isAscendingOrder,appliedMethods) }
                    isDialogOpen = false
                    onDismiss()
                },
                content = { Text(text = "No") }
            )
        },
        confirmButton = {
            TextButton(
                enabled = !waitingForDetails,
                content = {
                    Text(
                        text = "Wait",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    waitingForDetails = true

                    coroutineScope.launch {
                        onFetch()

                        isDialogOpen = false

                        changeEnabled(false)
                        val job = launch { onSort(isAscendingOrder,appliedMethods) }
                        onDismiss()

                        job.join()
                        changeEnabled(true)
                    }
                }
            )
        }
    )
}

@Composable
private fun ifSelectedLeadingIcon(selected : Boolean,label : String): (@Composable () -> Unit)? = if(selected) {{
    Icon(
        imageVector = Icons.Filled.Done,
        contentDescription = "Selected $label",
        modifier = Modifier.size(FilterChipDefaults.IconSize)
    )
}} else null

fun <T> createCompartorFrom(
    isAscending : Boolean,
    sortMethods : List<Int>,
    create : (it : T,method : Int) -> Comparable<*>
): Comparator<T> {
    val firstValue = sortMethods[0]
    var compartor = if(isAscending) compareBy<T> { create(it,firstValue) } else compareByDescending { create(it,firstValue) }

    sortMethods.subList(1,sortMethods.size).forEach { sortMethod ->
        compartor = compartor.thenBy {
            create(it,sortMethod)
        }
    }

    return compartor
}