package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.runtime.Stable

@Stable
enum class StackableSnackbarDuration(internal val duration : kotlin.Long) {
    Short(4000L),
    Long(10000L),
}