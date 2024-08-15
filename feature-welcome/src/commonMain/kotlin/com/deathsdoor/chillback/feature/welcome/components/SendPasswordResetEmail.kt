package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.feature.welcome.states.EmailState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
internal inline fun SendPasswordResetEmail(navController: NavController,state : EmailState)
    = SendPasswordResetEmail(state) { navController.popBackStack() }

@Composable
internal fun SendPasswordResetEmail(state : EmailState,goBack : () -> Unit) {
    val snackbarState = LocalSnackbarState.current
    val coroutineScope = rememberCoroutineScope()

    LazyResource {
        Button(
            content = {
                Text(stringResource(Res.strings.send_password_reset_email))
            },
            onClick = {
                coroutineScope.launch {
                    try {
                        Firebase.auth.sendPasswordResetEmail(state.email)
                        snackbarState.showInfoSnackbar(
                            title = stringResource(Res.strings.send_password_reset_email_successful)
                        )

                        delay(1500)
                        goBack()
                    }catch (exception : Exception) {
                        snackbarState.showErrorSnackbar(
                            title = stringResource(Res.strings.failed_send_password_reset_email)
                        )
                    }
                }
            }
        )
    }
}