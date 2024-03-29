package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.core.rememberDragSelectState
import com.dragselectcompose.extensions.dragSelectToggleableItem
import com.dragselectcompose.grid.LazyDragSelectVerticalGrid
import com.dragselectcompose.grid.indicator.AnimateSelectionDefaults
import com.dragselectcompose.grid.indicator.AnimateSelectionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO : On multiselect show fab + extra options
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun<T> LazyDismissibleSelectableList(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope,
    draggableState : DragSelectState<T> = rememberDragSelectState(),
    items : List<T>?,
    key: (item: T) -> Any,
    isSingleItemPerRow : Boolean,
    startToEndColor : Color = Color.Unspecified,
    endToStartColor : Color = Color.Unspecified,
    confirmValueChange: (SwipeToDismissBoxValue,item : T) -> Boolean,
    swipeableContent : @Composable (isStartToEnd : Boolean,item : T) -> Unit,
    optionContent : @Composable (index : Int,item : T,onDismiss : () -> Unit) -> Unit,
    content : @Composable (contentModifier : Modifier,item : T,isSelected : Boolean?,onLongClick : (() -> Unit)?) -> Unit,
    placeHolder : @Composable () -> Unit
) {
    if(items.isNullOrEmpty()) {
        placeHolder()
        return
    }

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

    val windowAdaptiveInfo = LocalWindowAdaptiveSize.current
    val itemContents : @Composable (contentModifier : Modifier,item : T,isSelected : Boolean?) -> Unit

    when(windowAdaptiveInfo.widthSizeClass == WindowWidthSizeClass.Expanded &&  windowAdaptiveInfo.heightSizeClass != WindowHeightSizeClass.Compact){
        // Desktop / Large Screens
        true -> {
            itemContents = @Composable { contentModifier ,item ,isSelected ->
                content(contentModifier,item,isSelected,null)
            }
        }

        false -> {
            // null -> no item select == isSheetShown == false
            // else -> isSheetShown == true
            var itemIndex : Int? by remember { mutableStateOf(null) }
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            itemContents = @Composable { contentModifier, item, isSelected ->
                val currentItem by rememberUpdatedState(newValue = item)
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { confirmValueChange(it, item) }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = isSingleItemPerRow,
                    enableDismissFromEndToStart = isSingleItemPerRow,
                    content = {
                        content(
                            contentModifier,
                            item,
                            isSelected
                        ) {
                            itemIndex = items.indexOf(item)
                            coroutineScope.launch { sheetState.show() }
                        }
                    },
                    backgroundContent = {
                        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) return@SwipeToDismissBox
                        val isStartToEnd =
                            dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd

                        val backgroundColorNotAnimated: Color
                        val alignment: Alignment

                        if (isStartToEnd) {
                            backgroundColorNotAnimated = startToEndColor
                            alignment = Alignment.CenterStart
                        } else {
                            backgroundColorNotAnimated = endToStartColor
                            alignment = Alignment.CenterEnd
                        }

                        val backgroundColor by animateColorAsState(backgroundColorNotAnimated)

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor),
                            contentAlignment = alignment,
                            content = { swipeableContent(isStartToEnd, currentItem) }
                        )
                    }
                )
            }

            itemIndex?.let {
                val item = items[it]
                optionContent(it,item) {
                    itemIndex = null
                    coroutineScope.launch { sheetState.hide() }
                }
            }
        }
    }


    LazyDragSelectVerticalGrid(
        modifier = modifier,
        items = items,
        state = draggableState,
        columns = GridCells.Fixed(count = gridCellCount),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        content = {
            items(key = key) { item ->
                val isSelected by remember {
                    derivedStateOf {  if(draggableState.inSelectionMode) draggableState.isSelected(item)
                    else null }
                }

                val contentModifier = Modifier
                    .applyIf(isSelected != null) {
                        dragSelectToggleableItem(
                            state = draggableState,
                            item = item,
                            semanticsLabel = "Select",
                            interactionSource = MutableInteractionSource(),
                        ).animateSelection(isSelected!!, AnimateSelectionDefaults.Default)
                    }


                itemContents(
                    contentModifier,
                    item,
                    isSelected
                )
            }
        }
   )
}

private fun Modifier.animateSelection(
    selected: Boolean,
    options: AnimateSelectionOptions = AnimateSelectionDefaults.Default,
): Modifier = composed {
    val transition = updateTransition(selected, label = "selected")
    val padding by transition.animateDp(label = "padding") { selected ->
        if (selected) options.padding else 0.dp
    }
    val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
        if (selected) options.cornerRadius else 0.dp
    }

    this.padding(padding).clip(RoundedCornerShape(roundedCornerShape))
}