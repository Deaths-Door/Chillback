package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
internal enum class SnackbarType(val icon: ImageVector, val color: Color) {
    Info(SnackbarInfoIcon, SnackbarInfo),
    Warning(SnackbarWarningIcon, SnackbarWarning),
    Error(SnackbarErrorIcon, SnackbarError),
    Success(SnackbarSuccessIcon,SnackbarSuccess),
}