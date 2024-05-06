package com.deathsdoor.chillback.ui.components.auth

import StackedSnackbarDuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.deathsdoor.chillback.ui.state.AuthState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spr.jetpack_loading.components.indicators.BallPulseRiseIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthState.AuthButton(
    modifier : Modifier = Modifier,
    style : TextStyle,
    coroutineScope : CoroutineScope,
    navigationToApp : () -> Unit
) {

    val snackbarState = LocalSnackbarState.current

    var isLoading by remember { mutableStateOf(false) }

    AnimatedContent(targetState = isLoading) {
        when(it) {
            true -> Row(verticalAlignment = Alignment.CenterVertically) {
                BallPulseRiseIndicator()
                Text(text = "Loading..")
            }
            false ->  Button(
                modifier = modifier,
                content = {
                    Text(
                        text = screenTitle,
                        style = style,
                    )
                },
                onClick = {
                    if(!isEmailValid) {
                        showInvalidSnackbar(snackbarState)
                        return@Button
                    }

                    if(isPasswordStrongEnough != null) {
                        snackbarState.showSuccessSnackbar(
                            title = "Weak Password",
                            description = "The password you entered is not strong enough." +
                                    " Please create a stronger password that meets the following criteria: $isPasswordStrongEnough",
                            duration = StackedSnackbarDuration.Long
                        )
                        return@Button
                    }

                    coroutineScope.launch {
                        if(isLoginShown) {
                            try {
                                isLoading = true
                                val authResult = Firebase.auth.signInWithEmailAndPassword(email,password).await()
                                isLoading = false
                                this@AuthButton.showSuccessAuth(snackbarState,authResult)
                                delay(1000L)
                                navigationToApp()
                            }
                            catch(exception : Exception) {
                                snackbarState.showSuccessSnackbar(
                                    title = "Log-in Failed",
                                    description = "We encountered an issue while logging you in. Please try again later. (Error: ${exception.localizedMessage ?: "An error occurred during login."})",
                                    duration = StackedSnackbarDuration.Long
                                )
                            }
                            return@launch
                        }

                        isLoading = true
                        val signInMethods = Firebase.auth.fetchSignInMethodsForEmail(email).await().signInMethods

                        if(signInMethods.isNullOrEmpty()) {
                            showEmailInUseSnackbar(snackbarState)
                            return@launch
                        }

                        try {
                            val authResult = Firebase.auth.createUserWithEmailAndPassword(email,password).await()
                            isLoading = false
                            this@AuthButton.showSuccessAuth(snackbarState,authResult)
                        }
                        catch(exception : Exception) {
                            snackbarState.showSuccessSnackbar(
                                title = "Account Creation Failed",
                                description = "We're sorry, but account creation failed. Please try again later. (Error: ${exception.localizedMessage ?: "An error occurred during account creation."})" ,
                                duration = StackedSnackbarDuration.Long
                            )
                        }
                    }
                }
            )
        }
    }

}
