package com.deathsdoor.chillback.core.media.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.core.media.resources.Res
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun RequestRingtonePermissions(content : @Composable () -> Unit){
    val ringtonePermissionState = rememberPermissionState(permission = android.Manifest.permission.WRITE_SETTINGS)

    when(val status = ringtonePermissionState.status){
        is PermissionStatus.Granted -> content()
        is PermissionStatus.Denied -> when(status.shouldShowRationale) {
            true -> LaunchedEffect(Unit) {
                ringtonePermissionState.launchPermissionRequest()
            }
            false -> LazyResource {
                val snackBarState = LocalSnackbarState.current
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    snackBarState.showInfoSnackbar(
                        title = stringResource(Res.strings.permission_required_for_feature),
                        actionTitle = stringResource(Res.strings.go_to_settings),
                        action = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )

                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}