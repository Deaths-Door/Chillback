package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun StackedSnackbarHost(
    hostState: StackableSnackbarState,
    modifier: Modifier = Modifier,
) {
    val firstItemVisible by hostState.newSnackbarHosted.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(hostState.currentSnackbarData) {
        val data = hostState.currentSnackbarData
        data.firstOrNull()?.let {
            delay(it.showDuration.duration)
            if (data.size == 1) {
                hostState.newSnackbarHosted.value = false
                delay(500)
                hostState.currentSnackbarData.remove(it)
                hostState.newSnackbarHosted.value = true
            }
        }
    }
    if (hostState.currentSnackbarData.isNotEmpty()) {
        StackableSnackbar(
            state = hostState,
            onSnackbarRemoved = {
                hostState.newSnackbarHosted.value = false
                coroutineScope.launch {
                    delay(500)
                    hostState.currentSnackbarData.removeLastOrNull()
                    hostState.newSnackbarHosted.value = true
                }
            },
            firstItemVisibility = firstItemVisible,
            maxStack = hostState.maxStack,
            animation = hostState.animation,
            modifier = modifier,
        )
    }
}

/// Source Code from https://github.com/rizmaulana/compose-stacked-snackbar with M3 support and WasmJs Support and certain API changes
@Stable
class StackableSnackbarState constructor(
    private val coroutinesScope: CoroutineScope,
    internal val animation: StackableSnackbarAnimation,
    internal val maxStack: Int = Int.MAX_VALUE
) {
    internal var currentSnackbarData = mutableStateListOf<StackableSnackbarData>()
    internal val newSnackbarHosted = MutableStateFlow(false)

    fun showInfoSnackbar(
        title: String,
        description: String? = null,
        actionTitle: String? = null,
        action: (() -> Unit)? = null,
        duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) {
        showSnackbar(
            data =
            StackableSnackbarData.Normal(
                SnackbarType.Info,
                title,
                description,
                actionTitle,
                action,
                duration,
            ),
        )
    }

    fun showSuccessSnackbar(
        title: String,
        description: String? = null,
        actionTitle: String? = null,
        action: (() -> Unit)? = null,
        duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) {
        showSnackbar(
            data =
            StackableSnackbarData.Normal(
                SnackbarType.Success,
                title,
                description,
                actionTitle,
                action,
                duration,
            ),
        )
    }

    fun showWarningSnackbar(
        title: String,
        description: String? = null,
        actionTitle: String? = null,
        action: (() -> Unit)? = null,
        duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) {
        showSnackbar(
            data =
            StackableSnackbarData.Normal(
                SnackbarType.Warning,
                title,
                description,
                actionTitle,
                action,
                duration,
            ),
        )
    }

    fun showErrorSnackbar(
        title: String,
        description: String? = null,
        actionTitle: String? = null,
        action: (() -> Unit)? = null,
        duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) {
        showSnackbar(
            data =
            StackableSnackbarData.Normal(
                SnackbarType.Error,
                title,
                description,
                actionTitle,
                action,
                duration,
            ),
        )
    }

    fun showCustomSnackbar(
        content: @Composable (() -> Unit) -> Unit,
        duration: StackableSnackbarDuration = StackableSnackbarDuration.Short,
    ) {
        showSnackbar(
            data = StackableSnackbarData.Custom(content, duration),
        )
    }

    private fun showSnackbar(data: StackableSnackbarData) {
        newSnackbarHosted.value = false
        currentSnackbarData.add(data)
        coroutinesScope.launch {
            delay(1_00)
            newSnackbarHosted.value = true
        }
    }
}



