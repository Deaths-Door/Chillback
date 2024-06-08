package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
internal sealed class StackableSnackbarData(internal val showDuration: StackableSnackbarDuration) {
    data class Normal(
        val type: SnackbarType,
        val title: String,
        val description: String? = null,
        val actionTitle: String? = null,
        val action: (() -> Unit)? = null,
        val duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) : StackableSnackbarData(duration)

    data class Custom(
        val content: @Composable (() -> Unit) -> Unit,
        val duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) : StackableSnackbarData(duration)
}
