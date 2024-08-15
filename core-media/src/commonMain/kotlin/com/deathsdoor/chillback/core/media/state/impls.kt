package com.deathsdoor.chillback.core.media.state

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import com.deathsdoor.chillback.core.layout.extensions.applyOn
import com.deathsdoor.chillback.core.layout.extensions.applyOnNotNull

@Composable
fun<T> rememberLazySelectableState(): LazySelectableState<T> = remember { LazySelectableState() }

@Composable
fun<T> LazySelectableState<T>.rememberIsSelected(item :T) = remember {
    derivedStateOf {  if(inSelectionMode) isSelected(item) else null }
}

/**
 * Adds a long click semantics to the modifier.
 *
 * When the semantics is triggered, the [item] is added to the selection in [DragSelectState].
 *
 * @param[T] the type of the items in the selection.
 * @param[selectState] the state to use for the semantics.
 * @param[item] the item to add to the selection when the semantics is triggered.
 * @param[label] the label to use for the semantics.
 */
public fun <T> Modifier.selectSemantics(
    selectableState : LazySelectableState<T>,
    item: T,
    label: () -> String,
): Modifier = selectSemantics(selectableState.inSelectionMode, label) {
    selectableState.addSelected(item)
}

/**
 * Adds a long click semantics to the modifier.
 *
 * @param[T] the type of the items in the selection.
 * @param[selectState] the state to use for the semantics.
 * @param[label] the label to use for the semantics.
 * @param[onLongClick] the callback to invoke when the semantics is triggered.
 */
public fun <T> Modifier.selectSemantics(
    selectableState : LazySelectableState<T>,
    label: () -> String,
    onLongClick: () -> Unit,
): Modifier = selectSemantics(selectableState.inSelectionMode, label, onLongClick)

/**
 * Adds a long click semantics to the modifier.
 *
 * @param[inSelectionMode] whether the semantics should be added or not.
 * @param[label] the label to use for the semantics.
 * @param[onLongClick] the callback to invoke when the semantics is triggered.
 */
fun Modifier.selectSemantics(
    inSelectionMode: Boolean,
    label: () -> String,
    onLongClick: () -> Unit,
): Modifier = applyOn(!inSelectionMode) {
    then(
        Modifier.semantics {
            this@semantics.onLongClick(label = label()) {
                onLongClick()
                true
            }
        }
    )
}

/**
 * A toggleable modifier that is only enabled when [DragSelectState.inSelectionMode] is true.
 *
 * This is useful for enabling selection when the user is in selection mode.
 *
 * @param[state] The [DragSelectState] that will be used to determine if the user is
 * in selection mode and selection state of item.
 * @param[item] The item that will be selected or deselected when the toggleable is toggled.
 * @param[interactionSource] the [MutableInteractionSource] that will be used to
 * emit `PressInteraction.Press` when this toggleable is being pressed.
 */
public fun <T> Modifier.selectToggleable(
    selectableState: LazySelectableState<T>,
    selected: Boolean?,
    item : T,
    interactionSource: MutableInteractionSource,
): Modifier = selectToggleable(
    selected = selected,
    interactionSource = interactionSource,
    onToggle = { toggled ->
        if (toggled) selectableState.addSelected(item)
        else selectableState.removeSelected(item)
    }
)


/**
 * A toggleable modifier that is only enabled when [inSelectionMode] is true.
 *
 * This is useful for enabling selection when the user is in selection mode.
 *
 * @param[inSelectionMode] Whether the user is in selection mode.
 * @param[selected] Whether the item is selected.
 * @param[interactionSource] the [MutableInteractionSource] that will be used to
 * emit `PressInteraction.Press` when this toggleable is being pressed.
 * @param[onToggle] Called when the toggleable is toggled.
 */
@Suppress("NAME_SHADOWING")
public fun Modifier.selectToggleable(
    selected: Boolean?,
    interactionSource: MutableInteractionSource,
    onToggle: (toggled: Boolean) -> Unit,
): Modifier = applyOnNotNull(selected) { selected ->
    then(
        Modifier.toggleable(
            value = selected,
            interactionSource = interactionSource,
            indication = null,
            onValueChange = onToggle
        )
    )
}