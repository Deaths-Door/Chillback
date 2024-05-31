package com.deathsdoor.chillback.core.layout.extensions

import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.ui.Modifier

fun Modifier.applyOn(condition : Boolean, transform : @DisallowComposableCalls Modifier.() -> Modifier)
        = if(condition) transform() else this

fun<T> Modifier.applyOnNull(value : T?,transform : @DisallowComposableCalls Modifier.() -> Modifier)
        = if(value == null) transform() else this

fun<T> Modifier.applyOnNotNull(value : T?,transform : @DisallowComposableCalls Modifier.(T) -> Modifier)
        = if(value != null) transform(value) else this
