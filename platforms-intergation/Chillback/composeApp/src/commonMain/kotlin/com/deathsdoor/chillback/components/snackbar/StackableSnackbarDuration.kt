package com.deathsdoor.chillback.components.snackbar

import androidx.compose.runtime.Stable

@Stable
enum class StackableSnackbarDuration(internal val duration : kotlin.Long) {
    Short(4000L),
    Long(10000L),
    Indefinite(kotlin.Long.MAX_VALUE),
}