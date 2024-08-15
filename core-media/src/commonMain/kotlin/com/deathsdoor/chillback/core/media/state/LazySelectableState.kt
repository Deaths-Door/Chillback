package com.deathsdoor.chillback.core.media.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf


// https://github.com/jordond/drag-select-compose/blob/main/core/src/commonMain/kotlin/com/dragselectcompose/core/DragSelectState.kt
@Stable
class LazySelectableState<T>() {
    /**
     * The state containing the selected items.
     */
    private var selectedItemsState = mutableStateListOf<T>()

    /**
     * Whether or not the grid is in selection mode.
     */
    val inSelectionMode: Boolean get() = selectedItemsState.isNotEmpty()

    /**
     * Whether or not the provided item is selected.
     *
     * @param[item] The item to check.
     * @return Whether or not the item is selected.
     */
    fun isSelected(item: T): Boolean = selectedItemsState.contains(item)

    /**
     * Adds the provided item to the selected items.
     *
     * @param[item] The item to add.
     */
    fun addSelected(item: T) {
        selectedItemsState.add(item)
    }

    /**
     * Removes the provided item from the selected items.
     *
     * @param[item] The item to remove.
     */
    fun removeSelected(item: T) {
        selectedItemsState.remove(item)
    }
}