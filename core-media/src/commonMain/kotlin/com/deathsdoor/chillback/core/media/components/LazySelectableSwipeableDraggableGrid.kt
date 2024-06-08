package com.deathsdoor.chillback.core.media.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.AdaptiveLayoutGeneric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyGridState

private typealias ContentLambda<T> = @Composable ReorderableCollectionItemScope.(item : T,interactionSource : MutableInteractionSource,elevation : Dp,onLongClick : (() -> Unit)?) -> Unit

// TODO; add https://medium.com/androiddevelopers/create-a-photo-grid-with-multiselect-behavior-using-jetpack-compose-9a8d588a9b63 like onedrive mobile
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun<T> LazySelectableSwipeableDraggableGrid(
    modifier : Modifier = Modifier,
    items : List<T>?,
    key: (item: T) -> Any,
    gridState: LazyGridState,
    @Suppress("SpellCheckingInspection")
    reorderableLazyGridState : ReorderableLazyGridState,
    coroutineScope: CoroutineScope,
    isSingleItemPerRow : Boolean,
    startToEndColor : Color = Color.Unspecified,
    endToStartColor : Color = Color.Unspecified,
    confirmValueChange: SwipeToDismissBoxValue.(item : T) -> Boolean,
    placeHolder : @Composable () -> Unit,
    swipeableContent : @Composable (isStartToEnd : Boolean,item : T) -> Unit,
    optionContent : @Composable (index : Int,item : T,onDismiss : () -> Unit) -> Unit,
    content : ContentLambda<T>
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


    val adaptiveItemContent = AdaptiveLayoutGeneric<ContentLambda<T>>(
        onDesktop = { { item, interactionSource, elevation , _ ->
            content(item, interactionSource, elevation, null)
        } },
        onMobile = {
            var itemIndex : Int? by remember { mutableStateOf(null) }
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            itemIndex?.let {
                val item = items[it]
                optionContent(it,item) {
                    itemIndex = null
                    coroutineScope.launch { sheetState.hide() }
                }
            }

            return@AdaptiveLayoutGeneric { item, interactionSource, elevation, _ ->
                val currentItem by rememberUpdatedState(newValue = item)
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { it.confirmValueChange(item) }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = isSingleItemPerRow,
                    enableDismissFromEndToStart = isSingleItemPerRow,
                    content = {
                        content(item, interactionSource, elevation)  {
                            itemIndex = items.indexOf(item)
                            coroutineScope.launch { sheetState.show() }
                        }
                    },
                    backgroundContent = {
                        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) return@SwipeToDismissBox

                        val isStartToEnd = dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
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
        }
    )

    LazyVerticalGrid(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Fixed(count = gridCellCount),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        content = {
            items(items = items,key = key) { item ->
                ReorderableItem(
                    state = reorderableLazyGridState,
                    key = key(item),
                    content = { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                        val interactionSource = remember { MutableInteractionSource() }
                        adaptiveItemContent(item, interactionSource, elevation,null)
                    }
                )
            }
        }
    )
}