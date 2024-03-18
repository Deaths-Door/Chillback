package com.deathsdoor.chillback.ui.components.auth

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalSuccessSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.deathsdoor.chillback.ui.state.AuthState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthState.AuthButton(
    modifier : Modifier = Modifier,
    style : TextStyle,
    coroutineScope : CoroutineScope
) {

    val errorSnackbar = LocalErrorSnackbarState.current
    val successSnackbar = LocalSuccessSnackbarState.current

    Button(
        modifier = modifier,
        content = {
            Text(
                text = screenTitle,
                style = style,
            )
        },
        onClick = {
            coroutineScope.launch {
                if(!isEmailValid) {
                    showInvalidSnackbar(errorSnackbar)
                    return@launch
                }

                val (isStrong,message) = isPasswordStrongEnough

                if(!isStrong) {
                    errorSnackbar.showSnackbar(message!!)
                    return@launch
                }

                if(isLoginShown) {
                    try {
                        Firebase.auth.signInWithEmailAndPassword(email,password).await()
                        successSnackbar.showSnackbar("Log-in Successful!")
                    }
                    catch(exception : Exception) {
                        errorSnackbar.showSnackbar("Log-in Failed: ${exception.localizedMessage}")
                    }
                    return@launch
                }

                val signInMethods = Firebase.auth.fetchSignInMethodsForEmail(email).await().signInMethods

                if(signInMethods.isNullOrEmpty()) {
                    showEmailInUseSnackbar(errorSnackbar)
                    return@launch
                }

                try {
                    Firebase.auth.createUserWithEmailAndPassword(email,password).await()
                    successSnackbar.showSnackbar("Account created successfully!")
                }
                catch(exception : Exception) {
                    errorSnackbar.showSnackbar("Account Creation Failed: ${exception.localizedMessage}")
                }
            }
        }
    )
}
