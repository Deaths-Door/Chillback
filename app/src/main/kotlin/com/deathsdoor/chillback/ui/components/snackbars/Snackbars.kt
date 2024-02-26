package com.deathsdoor.chillback.ui.components.snackbars

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalWarningSnackbarState

@Composable
fun CreateSnackbarHosts() {
    val modifier = Modifier.padding(8.dp)
    val error = MaterialTheme.colorScheme.onErrorContainer
    SnackbarHost(
        hostState = LocalErrorSnackbarState.current,
        modifier = modifier,
        snackbar = { Toast(it, error) }
    )

    SnackbarHost(
        hostState = LocalWarningSnackbarState.current,
        modifier = modifier,
        snackbar = { Toast(it, Color.Yellow) }
    )
}

@Composable
private fun Toast(data: SnackbarData,backgroundColor : Color) = Snackbar(
    snackbarData = data,
    shape = CircleShape,
    containerColor = backgroundColor,
)