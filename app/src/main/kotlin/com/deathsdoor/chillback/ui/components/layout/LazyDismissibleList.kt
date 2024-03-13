package com.deathsdoor.chillback.ui.components.layout

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun rememberSelectedIDsOrNotInMultiSelectMode(): MutableState<Set<Long>?> = rememberSaveable { mutableStateOf(null) }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun<T> LazyDismissibleList(
    modifier : Modifier = Modifier,
    items : List<T>?,
    key: (index: Int, item: T) -> Any,
    isSingleItemPerRow : Boolean,
    selectedIDs: MutableState<Set<Long>?>,
    confirmValueChange: (SwipeToDismissBoxValue,index : Int,item : T) -> Boolean,
    startToEndColor : Color = Color.Unspecified,
    endToStartColor : Color = Color.Unspecified,
    swipeableContent : @Composable (isStartToEnd : Boolean,item : T) -> Unit,
    placeHolder : @Composable () -> Unit,
    optionContent : @Composable (optionState : ModalOptionsState,index : Int,item : T) -> Unit,
    content : @Composable RowScope.(index : Int,item : T,isSelected : Boolean?,onLongClick : () -> Unit) -> Unit
) {
    if (items.isNullOrEmpty()) {
        placeHolder()
        return
    }

    val optionState = rememberModalOptionsState(skipPartiallyExpanded = true)

    var itemIndex: Int? by remember { mutableStateOf(null) }

    val gridCellCount : Int
    val verticalArrangement : Arrangement.Vertical
    val horizontalArrangement : Arrangement.Horizontal

    if(isSingleItemPerRow) {
        gridCellCount = 1
        verticalArrangement = Arrangement.Top
        horizontalArrangement = Arrangement.Start
    }
    else {
        gridCellCount = 2
        val arrangement =  Arrangement.spacedBy(12.dp)
        verticalArrangement = arrangement
        horizontalArrangement = arrangement
    }

    val state = rememberLazyGridState()
    val autoScrollSpeed = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(autoScrollSpeed.floatValue) {
        if(autoScrollSpeed.floatValue == 0f) return@LaunchedEffect

        while(isActive) {
            state.scrollBy(autoScrollSpeed.floatValue)
            delay(10)
        }
    }

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(count = gridCellCount),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier.multiSelectDragHandler(
            state = state,
            selectedIDs = selectedIDs,
            autoScrollSpeed = autoScrollSpeed,
            autoScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
        ),
        content = {
            itemsIndexed(items,key) { index , item ->
                val currentItem by rememberUpdatedState(item)
                val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
                    confirmValueChange(it,index,currentItem) }
                )
                val isSelected by remember {
                    derivedStateOf { selectedIDs.value?.contains(key(index,item)) }
                }

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = isSingleItemPerRow,
                    enableDismissFromEndToStart = isSingleItemPerRow,
                    content = {
                        content(index,currentItem,isSelected) {
                            itemIndex = index
                            optionState.show()
                        }
                    },
                    backgroundContent = {
                        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) return@SwipeToDismissBox
                        val isStartToEnd = dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd

                        val backgroundColorNotAnimated : Color
                        val alignment : Alignment

                        if(isStartToEnd){
                            backgroundColorNotAnimated = startToEndColor
                            alignment = Alignment.CenterStart
                        }
                        else {
                            backgroundColorNotAnimated = endToStartColor
                            alignment = Alignment.CenterEnd
                        }

                        val backgroundColor by animateColorAsState(backgroundColorNotAnimated)

                        Box(
                            modifier = Modifier.fillMaxSize().background(color = backgroundColor),
                            contentAlignment = alignment,
                            content = { swipeableContent(isStartToEnd,currentItem) }
                        )
                    }
                )
            }
        }
    )

    itemIndex?.let {
        val item = items[it]
        optionContent(optionState,it,item)
    }
}

private fun Modifier.multiSelectDragHandler(
    state: LazyGridState,
    selectedIDs: MutableState<Set<Long>?>,
    autoScrollSpeed: MutableState<Float>,
    autoScrollThreshold: Float,
) = pointerInput(Unit) {
    fun LazyGridState.itemAt(offset : Offset) : Int? = layoutInfo.visibleItemsInfo.find { itemInfo ->
        itemInfo.size.toIntRect().contains(offset.round() - itemInfo.offset)
    }?.key as? Int

    var initialKey : Long? = null
    var currentKey : Long? = null

    // TODO : Change it so that its only run if is in selection mode
    detectDragGesturesAfterLongPress(
        onDragCancel = { initialKey = null; autoScrollSpeed.value = 0f },
        onDragEnd = { initialKey = null; autoScrollSpeed.value = 0f },
        onDragStart = { offset ->
            Log.d("multiselect","drag start ${selectedIDs.value}")
            Log.d("multiselect","drag registering for ${state.itemAt(offset)}")
            state.itemAt(offset)?.let { key ->
                if (!selectedIDs.value!!.contains(key.toLong())) {
                    initialKey = key.toLong()
                    currentKey = key.toLong()
                    selectedIDs.value = selectedIDs.value?.plus(key.toLong())
                }
            }

            Log.d("multiselect","initialKey = $initialKey , ${selectedIDs.value}")

        },
        onDrag = { change, _ ->
            if (initialKey != null) {
                val distFromBottom =
                    state.layoutInfo.viewportSize.height - change.position.y
                val distFromTop = change.position.y
                autoScrollSpeed.value = when {
                    distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                    distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                    else -> 0f
                }

                state.itemAt(change.position)?.let { key ->
                    if (currentKey != key.toLong()) {
                        selectedIDs.value = selectedIDs.value!!
                            .minus(initialKey!!..currentKey!!)
                            .minus(currentKey!!..initialKey!!)
                            .plus(initialKey!!..key)
                            .plus(key..initialKey!!)
                        currentKey = key.toLong()
                    }

                    Log.d("multiselect","currentKey = $currentKey , ${selectedIDs.value}")

                }

                Log.d("multiselect","onDrag ending")

            }
        }
        /*onDragStart = { offset ->
            Log.d("multiselect","drag start ${selectedIDs.value}")
            if(selectedIDs.value == null) return@detectDragGesturesAfterLongPress
            Log.d("multiselect","drag registering")

            state.itemAt(offset)?.let { _key ->
                val key = _key.toLong()

                if(selectedIDs.value!!.contains(key)) return@let

                initialKey = key
                currentKey = key
                selectedIDs.value = selectedIDs.value!! + key
            }

            Log.d("multiselect","initialKey = $initialKey , ${selectedIDs.value}")
        },
        onDrag = { change , _ ->
            // Since we only set initial key if ids not null then it works
            if(initialKey == null) return@detectDragGesturesAfterLongPress

            val distFromBottom = state.layoutInfo.viewportSize.height - change.position.y
            val distFromTop = change.position.y

            autoScrollSpeed.value = when {
                distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                else -> 0f
            }

            state.itemAt(change.position)?.let { _key ->
                val key = _key.toLong()
                if(currentKey == key) return@let
                selectedIDs.value = selectedIDs.value!!
                    .minus(initialKey!!..currentKey!!)
                    .minus(currentKey!!..initialKey!!)
                    .plus(initialKey!!..key)
                    .plus(key..initialKey!!)
                currentKey = key
            }
        }*/
    )
}