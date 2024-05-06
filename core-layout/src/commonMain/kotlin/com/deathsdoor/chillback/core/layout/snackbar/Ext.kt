package com.deathsdoor.chillback.core.layout.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberStackableSnackbarHostState(
    coroutineScope: CoroutineScope,
    maxStack: Int = Int.MAX_VALUE,
    animation: StackableSnackbarAnimation = StackableSnackbarAnimation.Bounce,
) = remember {
    StackableSnackbarState(
        animation = animation,
        maxStack = maxStack,
        coroutinesScope = coroutineScope
    )
}