package com.deathsdoor.chillback.ui.components.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissionRationale(
    permissionState: PermissionState
) {
    if(!permissionState.status.shouldShowRationale) return

    var dialogState by remember { mutableStateOf(true) }

    if(!dialogState) return

    AlertDialog(
        onDismissRequest = { dialogState = false },
        text = { Text("This permission is essential for accessing this feature. Please enable it to fully enjoy this functionality.") },
        confirmButton = {
             Button(
                 onClick = { permissionState.launchPermissionRequest() },
                 content = { Text("Request") }
             )
        },
    )
}