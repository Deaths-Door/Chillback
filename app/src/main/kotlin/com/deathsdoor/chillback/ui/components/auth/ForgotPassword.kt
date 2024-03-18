package com.deathsdoor.chillback.ui.components.auth

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.state.AuthState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ForgotPassword(
    modifier : Modifier = Modifier,
    style : TextStyle,
    coroutineScope: CoroutineScope
) {
    var dialogState by remember { mutableStateOf(false) }

    ClickableText(
        modifier = modifier,
        text = AnnotatedString("Forgot Password"),
        style = style,
        onClick = { dialogState = true }
    )

    if(!dialogState) return

    val state = remember { AuthState() }
    val errorSnackbar = LocalErrorSnackbarState.current

    AlertDialog(
        onDismissRequest = { dialogState = false },
        title = { Text(text = "Email for Reset") },
        text = { state.EmailOutlinedTextField(style = style) },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        if(!state.isEmailValid) {
                            state.showInvalidSnackbar(errorSnackbar)
                            return@launch
                        }

                        Firebase.auth.sendPasswordResetEmail(state.email).await()
                    }
                },
                content = {
                    Text("Send")
                }
            )
        },
    )
}
