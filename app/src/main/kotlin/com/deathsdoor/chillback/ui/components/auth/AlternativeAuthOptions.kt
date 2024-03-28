package com.deathsdoor.chillback.ui.components.auth

import StackedSnackbarDuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundIconButton
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO : Add more providers in the future , mainly Apple && Microsoft && playgames
@Composable
fun ShortenAlternativeAuthOptions(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope,
    onSuccess: (AuthResult) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            GoogleAuthContentProvider(
                coroutineScope = coroutineScope,
                onSuccess = onSuccess,
                content = { name , painter, onClick  ->
                    CircularBackgroundIconButton(
                        painter = painter,
                        contentDescription = "Login with $name",
                        onClick = onClick
                    )
                }
            ) 
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FullAlternativeAuthOptions(
    modifier : Modifier = Modifier,
    style : TextStyle,
    coroutineScope: CoroutineScope,
    onSuccess: (AuthResult) -> Unit
) {
    FlowRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        GoogleAuthContentProvider(
            coroutineScope = coroutineScope,
            onSuccess = onSuccess,
            content = { name, painter, onClick ->
                FullSignInButton(name = name, painter = painter,onClick = onClick, style = style)
            }
        )
    }
}
@Composable
private fun GoogleAuthContentProvider(
    coroutineScope: CoroutineScope,
    content : @Composable (name : String,painter : Painter,onClick : () -> Unit) -> Unit,
    onSuccess: (AuthResult) -> Unit
) {
    val context = LocalContext.current
    val snackbarState = LocalSnackbarState.current

    val authLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            coroutineScope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onSuccess(authResult)
            }
        } catch (exception: ApiException) {
            val errorMessage = when (exception.statusCode) {
                CommonStatusCodes.INTERNAL_ERROR -> "An internal error occurred. Please try again later."
                CommonStatusCodes.NETWORK_ERROR -> "There seems to be a network issue. Please check your connection and try again."
                else -> exception.localizedMessage ?: "Login failed. Please try again."
            }
            snackbarState.showSuccessSnackbar(  // Assuming successSnackbar can be used for errors
                title = "Login Failed",
                description = errorMessage,
                duration = StackedSnackbarDuration.Long
            )
        }
    }

    content("Google",painterResource(id = R.drawable.google_logo)) {
        val gso =  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        authLauncher.launch(googleSignInClient.signInIntent)
    }
}
