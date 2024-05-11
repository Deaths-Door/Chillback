package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.deathsdoor.chillback.core.layout.LazyResource
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarDuration
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.feature.welcome.states.EmailState
import com.deathsdoor.chillback.feature.welcome.states.PasswordState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthenticationButton(
    isLoginScreen : Boolean,
    emailState: EmailState,
    passwordState: PasswordState,
    coroutineScope : CoroutineScope,
    modifier : Modifier = Modifier
) {
    val isLoading = remember { mutableStateOf(false) }

    val snackBarState = LocalSnackbarState.current

    LazyResource {
        ElevatedButton(
            modifier = modifier.fillMaxWidth(),
            content = {
                Text(text = stringResource(if(isLoginScreen) Res.strings.login else Res.strings.signin))
            },
            onClick = {
                emailState.informUserOnInvalidEmail(snackBarState, this@LazyResource) {
                    passwordState.informUserOnWeakPassword(snackBarState,this@LazyResource) {
                        isLoading.value = true

                        coroutineScope.launch {
                            when(isLoginScreen) {
                                true -> login(snackBarState, emailState, passwordState, isLoading)
                                false -> signIn(snackBarState, emailState, passwordState, isLoading)
                            }
                        }
                    }
                }
            }
        )
    }

    if(!isLoading.value) return

    BasicAlertDialog(
        onDismissRequest = { isLoading.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // TODO : https://github.com/MahboubehSeyedpour/jetpack-loading/blob/master/jetpack-loading BallPulseRiseIndicator()
                Text(text = stringResource(Res.strings.loading))
            }
        }
    )
}

private suspend fun LazyResourceLoader.login(
    snackBarState: StackableSnackbarState,
    emailState: EmailState,
    passwordState: PasswordState,
    isLoading : MutableState<Boolean>,
) = try {
    val authResult = Firebase.auth.signInWithEmailAndPassword(emailState.email,passwordState.password)
    onSuccessfulAuth(snackBarState, isLoading, authResult)
    delay(2500)
} catch(exception : Exception) {
    snackBarState.showErrorSnackbar(
        title = stringResource(Res.strings.failed_account_login),
        description = stringResource(Res.strings.failed_account_login_description),
        duration = StackableSnackbarDuration.Long
    )
}

private suspend fun LazyResourceLoader.signIn(
    snackBarState: StackableSnackbarState,
    emailState: EmailState,
    passwordState: PasswordState,
    isLoading : MutableState<Boolean>,
){
    try {
        val signInMethods = Firebase.auth.fetchSignInMethodsForEmail(emailState.email)

        if(signInMethods.isEmpty()) {
            snackBarState.showErrorSnackbar(
                title = stringResource(Res.strings.email_in_use),
                description = stringResource(Res.strings.email_in_use_description)
            )
            isLoading.value = false

            return
        }

        // Note : No Need to Verify Email here as WelcomeScreen has a VerifyEmail Composable attached that will handle all this
        val authResult = Firebase.auth.createUserWithEmailAndPassword(emailState.email,passwordState.password)
        onSuccessfulAuth(snackBarState, isLoading, authResult)
    } catch(exception : Exception) {
        snackBarState.showErrorSnackbar(
            title = stringResource(Res.strings.failed_account_creation),
            description = stringResource(Res.strings.failed_account_creation_description)
        )
    }
}

private fun LazyResourceLoader.onSuccessfulAuth(
    snackBarState: StackableSnackbarState,
    isLoading : MutableState<Boolean>,
    authResult : AuthResult
) {
    isLoading.value  = false

    snackBarState.showSuccessSnackbar(
        stringResource(Res.strings.successful_account_login,authResult.user!!.displayName ?: "")
    )
}
