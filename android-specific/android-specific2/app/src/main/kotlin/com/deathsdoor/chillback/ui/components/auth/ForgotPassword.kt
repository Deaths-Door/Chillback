package com.deathsdoor.chillback.ui.components.auth

import StackedSnackbarDuration
import android.app.Application
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.deathsdoor.chillback.ui.state.AuthState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator
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
    val snackbarState = LocalSnackbarState.current

    val application = if(LocalInspectionMode.current) null else LocalAppState.current.getApplication<Application>()

    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { dialogState = false },
        title = { Text(text = "Email for Reset") },
        text = {
            AnimatedContent(targetState = isLoading) {
                when(it) {
                    false -> state.EmailOutlinedTextField(style = style)
                    true -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Loading..")

                        Spacer(modifier = Modifier.width(16.dp))

                        BallClipRotatePulseIndicator()
                    }
                }
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(!state.isEmailValid) {
                        state.showInvalidSnackbar(snackbarState)
                        return@TextButton
                    }
                    coroutineScope.launch {
                        isLoading = true
                        Firebase.auth.sendPasswordResetEmail(state.email).await()
                        isLoading = false

                        snackbarState.showSuccessSnackbar(
                            title = "Email Sent!",
                            description = "We've sent a password reset email. Check your inbox and follow the instructions to reset your password.",
                            duration = StackedSnackbarDuration.Short,
                            actionTitle = "Go to Inbox",
                            action = {
                                application?.startActivity(
                                    Intent(Intent.ACTION_MAIN).apply {
                                        addCategory(Intent.CATEGORY_APP_EMAIL)
                                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                    }
                                )
                            }
                        )
                    }
                },
                content = {
                    Text("Send")
                }
            )
        },
    )
}
