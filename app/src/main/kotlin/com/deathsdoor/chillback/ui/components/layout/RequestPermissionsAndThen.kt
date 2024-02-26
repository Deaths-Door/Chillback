@file:OptIn(ExperimentalPermissionsApi::class)

package com.deathsdoor.chillback.ui.components.layout

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun RequestPermissionsAndThen(
    permissionState: PermissionState,
    onDismiss : () -> Unit,
    permissionRequiredReason : () -> String,
    content : @Composable () -> Unit
) {
    if(permissionState.status.isGranted) {
        content()
        return
    }

    val isPermanentlyDenied = permissionState.isPermanentlyDenied()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permission required") },
        text = {
            if(isPermanentlyDenied) Text("It seems you've previously denied ${permissionState.permission.substring("android.permission.".length).replace("_"," ")} permission for this app. To continue using this feature, please enable it in your app settings.")
            else Text(permissionRequiredReason())
        },
        confirmButton = {
            val context = LocalContext.current
            Button(
                onClick = { if(isPermanentlyDenied) context.findActivity().openApplicationSettings() else permissionState.launchPermissionRequest() },
                content = { Text(if(isPermanentlyDenied) "Enabled" else "Request") }
            )
        }
    )
}

private fun PermissionState.isPermanentlyDenied() = !status.shouldShowRationale && !status.isGranted

private fun Activity.openApplicationSettings() = Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", packageName, null)
).also(::startActivity)

private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}