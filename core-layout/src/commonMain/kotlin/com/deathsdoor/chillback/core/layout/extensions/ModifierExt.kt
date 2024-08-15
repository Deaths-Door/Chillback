package com.deathsdoor.chillback.core.layout.extensions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent

fun Modifier.applyOn(condition : Boolean, transform : @DisallowComposableCalls Modifier.() -> Modifier)
        = if(condition) transform() else this

fun<T> Modifier.applyOnNull(value : T?,transform : @DisallowComposableCalls Modifier.() -> Modifier)
        = if(value == null) transform() else this

fun<T> Modifier.applyOnNotNull(value : T?,transform : @DisallowComposableCalls Modifier.(T) -> Modifier)
        = if(value != null) transform(value) else this

/**
 * A modifier that adds focus handling to a composable. When clicked, it requests focus for the composable.
 */
fun Modifier.requestFocusOnClick() = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

    // https://stackoverflow.com/a/68863985
    focusRequester(focusRequester).focusable(interactionSource = interactionSource).clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = { focusRequester.requestFocus() }
    )
}


/**
 * A modifier that allows you to handle key events when the composable has focus.
 *
 * This modifier stores the focus state of the composable and only forwards key events to the provided
 * `onPreviewKeyEvent` lambda when the composable is focused.
 *
 * @param onPreviewKeyEvent A lambda that receives a [KeyEvent] and returns whether it was handled.
 *
 * **Note:** If you intend to use this modifier in conjunction with `requestFocusOnClick` on the same composable,
 * make sure to apply `onHasFocusPreviewKeyEvent` before `requestFocusOnClick`.
 */
fun Modifier.onHasFocusPreviewKeyEvent(onPreviewKeyEvent: (KeyEvent) -> Boolean) = composed {
    var hasFocus by remember { mutableStateOf(false) }
    onFocusChanged {
        hasFocus = it.hasFocus
    }
    .onKeyEvent {
        if(hasFocus) onPreviewKeyEvent(it) else false
    }
}
