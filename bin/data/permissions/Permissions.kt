package com.deathsdoor.chillback.data.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Permissions {
    private val SDK_VERSION = Build.VERSION.SDK_INT

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun rememberReadExternalStoragePermissionState() = rememberPermissionState(
        if(SDK_VERSION >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_AUDIO
        else android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @OptIn(ExperimentalPermissionsApi::class)
    fun managePermissionFlow(
        permissionState : PermissionState,
        coroutineScope : CoroutineScope,
        context: Context,
        infoSnackbarHostState: SnackbarHostState
    ) {
        if(permissionState.status.isGranted) return
        permissionState.launchPermissionRequest()

        if(permissionState.status.shouldShowRationale) return

        coroutineScope.launch {
            displayPermissionGrantInstructionSnackbar(
                context = context,
                infoSnackbarHostState = infoSnackbarHostState
            )
        }
    }

    private suspend fun displayPermissionGrantInstructionSnackbar(
        context : Context,
        infoSnackbarHostState : SnackbarHostState
    ) {
        val result = infoSnackbarHostState.showSnackbar(
            message = "Permission required",
            actionLabel = "Go to settings",
            duration = SnackbarDuration.Long
        )

        if(result != SnackbarResult.ActionPerformed) {
            infoSnackbarHostState.showSnackbar(
                message = "Sorry, this feature can not be used without the permission"
            )
            return
        }

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )

        context.startActivity(intent)
    }
}