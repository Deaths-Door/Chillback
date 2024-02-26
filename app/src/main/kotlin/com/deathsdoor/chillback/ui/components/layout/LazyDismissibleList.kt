package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.components.modaloptions.ModalOptionsState
import com.deathsdoor.chillback.ui.components.modaloptions.rememberModalOptionsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun<T> LazyDismissibleList(
    modifier : Modifier = Modifier,
    items : List<T>?,
    singleItemPerRow : Boolean,
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
    confirmValueChange: (DismissValue,index : Int,item : T) -> Boolean,
    startToEndColor : Color = Color.Unspecified,
    endToStartColor : Color = Color.Unspecified,
    swipeableContent : @Composable (isStartToEnd : Boolean,item : T) -> Unit,
    placeHolder : @Composable () -> Unit,
    optionContent : @Composable (optionState : ModalOptionsState,item : T) -> Unit,
    content : @Composable RowScope.(index : Int,item : T,onLongClick : () -> Unit) -> Unit
) {
    if (items.isNullOrEmpty()) {
        placeHolder()
        return
    }

    val optionState = rememberModalOptionsState(skipPartiallyExpanded = true)

    var itemIndex: Int? by remember { mutableStateOf(null) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = if(singleItemPerRow) 1 else 2),
        contentPadding = PaddingValues(vertical = 16.dp),
        content = {
            itemsIndexed(items) { index , item ->
                val currentItem by rememberUpdatedState(item)
                val dismissState = rememberDismissState(confirmValueChange = { confirmValueChange(it,index,currentItem) })

                SwipeToDismiss(
                    state = dismissState,
                    directions = directions,
                    dismissContent = {
                        content(index,currentItem) {
                            itemIndex = index
                            optionState.show()
                        }
                    },
                    background = {
                        if (dismissState.targetValue == DismissValue.Default) return@SwipeToDismiss

                        val isStartToEnd = dismissState.dismissDirection == DismissDirection.StartToEnd
                        val backgroundColor by animateColorAsState(if(isStartToEnd) startToEndColor else endToStartColor)

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor),
                            contentAlignment = if(isStartToEnd) Alignment.CenterStart else Alignment.CenterEnd,
                            content = { swipeableContent(isStartToEnd,currentItem) }
                        )
                    }
                )
            }
        }
    )

    itemIndex?.let {
        val item = items[it]
        optionContent(optionState,item)
    }
}