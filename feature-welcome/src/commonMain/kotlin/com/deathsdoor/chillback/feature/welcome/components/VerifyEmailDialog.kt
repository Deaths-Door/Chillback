package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun VerifyEmailDialog(onVerified  : suspend CoroutineScope.() -> Unit, content :@Composable () -> Unit) {
    val currentFirebaseUser by Firebase.auth.authStateChanged.collectAsState(null)

    content()

    if(currentFirebaseUser == null) return
    if(currentFirebaseUser!!.isEmailVerified) {
        LaunchedEffect(Unit,onVerified)
        return
    }

    LazyResource {
        val snackBarState = LocalSnackbarState.current

        LaunchedEffect(Unit) {
            this@LazyResource.sendEmailForVerification(
                coroutineScope = this,
                user = currentFirebaseUser!!,
                snackBarState = snackBarState
            )
        }

        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(Res.strings.verify_email)) },
            text = { Text(text = stringResource(Res.strings.verify_email_description)) },
            confirmButton = {
                val coroutineScope = rememberCoroutineScope()

                Button(
                    content = { Text(text = stringResource(Res.strings.resend_verification_email)) },
                    onClick = {
                        coroutineScope.launch {
                            this@LazyResource.sendEmailForVerification(
                                coroutineScope = this,
                                user = currentFirebaseUser!!,
                                snackBarState = snackBarState
                            )
                        }
                    }
                )
            }
        )
    }
}

private suspend fun LazyResourceLoader.sendEmailForVerification(
    coroutineScope: CoroutineScope,
    user : FirebaseUser,
    snackBarState: StackableSnackbarState
) : Unit = try {
    user.sendEmailVerification()

    snackBarState.showInfoSnackbar(
        title = stringResource(Res.strings.success_send_verification_email)
    )
}catch (exception : Exception) {
   snackBarState.showErrorSnackbar(
       title = stringResource(Res.strings.failed_send_verification_email),
       description = stringResource(Res.strings.failed_send_verification_email),
       action = {
           coroutineScope.launch {
               this@sendEmailForVerification.sendEmailForVerification(coroutineScope,user,snackBarState)
           }
       }
   )
}