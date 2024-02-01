package com.deathsdoor.chillback.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.BackButton
import com.deathsdoor.chillback.ui.components.layout.CenteredDivider
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundIconButton
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalSuccessSnackbarState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private class LoginState {
    var isLoginShown by mutableStateOf(true)
    val screenTitle by derivedStateOf { if(isLoginShown) "Log-in" else "Sign-in" }

    val email by mutableStateOf(EmailState())

    var password by mutableStateOf("")

    // yes or no and the message with it
    var isPasswordStrongEnough: Pair<Boolean, String?> by mutableStateOf(false to null)

    suspend fun login(successSnackbar : SnackbarHostState,errorSnackbar: SnackbarHostState) = try {
        Firebase.auth.signInWithEmailAndPassword(email.value,password).await()
        successSnackbar.showSnackbar("Log-in Successful!")
    }
    catch(exception : Exception) {
        errorSnackbar.showSnackbar("Log-in Failed: ${exception.localizedMessage}")
    }

    // TODO : Make user verify-email and then only let them
    suspend fun signIn(successSnackbar : SnackbarHostState,errorSnackbar: SnackbarHostState) = try {
        Firebase.auth.createUserWithEmailAndPassword(email.value,password).await()
        successSnackbar.showSnackbar("Account created successfully!")
    }
    catch(exception : Exception) {
        errorSnackbar.showSnackbar("Account Creation Failed: ${exception.localizedMessage}")
    }
}

private class EmailState {
    var value by mutableStateOf("")
    var isValid by mutableStateOf(false)

    suspend fun showInvalidSnackbar(errorSnackbar : SnackbarHostState) = errorSnackbar.showSnackbar("Email is not valid")

    suspend fun showEmailInUseSnackbar(errorSnackbar: SnackbarHostState) = errorSnackbar.showSnackbar("Sorry, email is already in use")

}

@Composable
fun AuthScreenOption(navController: NavController) {
    val state = remember { LoginState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            BackButton(modifier = Modifier.align(Alignment.Start)) { navController.popBackStack() }

            Text(
                modifier = Modifier.padding(top = 32.dp, bottom = 24.dp).background(Color.Red),
                text = "Let's get you in",
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmailOutlinedTextField(
                modifier = Modifier.padding(bottom = 16.dp),
                state = state.email
            )

            PasswordOutlinedTextField(
                modifier = Modifier.padding(bottom = 16.dp),
                state = state
            )

            //.. TODO then you can do account setup like user-name etc

            AuthButton(
                modifier = Modifier.fillMaxWidth(0.7f).padding(16.dp),
                state = state,
                coroutineScope = coroutineScope
            )


            if(state.isLoginShown) {
                ForgotPassword(
                    modifier= Modifier.padding(bottom = 8.dp).background(Color.Red),
                    coroutineScope = coroutineScope
                )

                CenteredDivider(
                    modifier = Modifier.fillMaxWidth(0.7f)
                        .padding(top = 16.dp,bottom = 24.dp)
                        .clip(CircleShape)
                )

                AlternativeAuthOptions(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            ToggleScreens(state = state)
        }
    )
}


@Composable
private fun EmailOutlinedTextField(modifier: Modifier = Modifier,state : EmailState) = OutlinedTextField(
    modifier = modifier,
    value = state.value,
    onValueChange = {
        state.value = it
        state.isValid = state.value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    },
    label = { Text(text = "Email") },
    leadingIcon = {
        Image(
            imageVector = Icons.Default.Email,
            contentDescription = null
        )
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    isError = state.value.isNotEmpty() && !state.isValid,
    singleLine = true
)

private fun passwordStrength(password: String): Pair<Boolean,String?> {
    val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')

    return when {
        password.length < 8 -> true to "Password must be at least 8 characters long."
        !password.any { it.isUpperCase() } -> true to "Password must contain at least one uppercase letter."
        !password.any { it.isLowerCase() } ->true to  "Password must contain at least one lowercase letter."
        !password.any { it.isDigit() } -> true to "Password must contain at least one digit."
        specialCharacters.none { it in password } -> true to "Password must contain at least one special character (!, @, #, $, %, ^, &, *)."
        else -> false to null
    }
}

@Composable
private fun PasswordOutlinedTextField(modifier: Modifier = Modifier,state: LoginState) = OutlinedTextField(
    modifier = modifier,
    value = state.password,
    onValueChange = {pwd ->
        state.apply {
            password = pwd
            isPasswordStrongEnough = passwordStrength(password)
        }
    },
    label = { Text(text = "Password") },
    leadingIcon = {
        Image(
            imageVector = Icons.Default.Edit,
            contentDescription = null
        )
    } ,
    visualTransformation = PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    textStyle = MaterialTheme.typography.bodyMedium,
    isError = state.password.isNotEmpty() && !state.isPasswordStrongEnough.first,
    singleLine = true
)

@Composable
private fun AuthButton(
    modifier : Modifier = Modifier,
    state : LoginState,
    coroutineScope : CoroutineScope
) {

    val errorSnackbar = LocalErrorSnackbarState.current
    val successSnackbar = LocalSuccessSnackbarState.current

    Button(
        modifier = modifier,
        content = {
            Text(
                text = state.screenTitle,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onClick = {
            coroutineScope.launch {
                if(!state.email.isValid) {
                    state.email.showInvalidSnackbar(errorSnackbar)
                    return@launch
                }

                val (isStrong,message) = state.isPasswordStrongEnough
                if(!isStrong) {
                    errorSnackbar.showSnackbar(message!!)
                    return@launch
                }

                if(state.isLoginShown) {
                    state.login(successSnackbar,errorSnackbar)
                    return@launch
                }

                val signInMethods = Firebase.auth.fetchSignInMethodsForEmail(state.email.value).await().signInMethods

                if(signInMethods == null || signInMethods.isNotEmpty()) {
                    state.email.showEmailInUseSnackbar(errorSnackbar)
                    return@launch
                }

                state.signIn(successSnackbar,errorSnackbar)
            }
        }
    )
}

@Composable
private fun AlternativeAuthOptions(modifier : Modifier = Modifier) {
    val items = listOf(
        Triple("Google",R.drawable.ic_launcher_foreground){ /*TODO*/ },
        Triple("Apple",R.drawable.ic_launcher_foreground){ /*TODO*/},
        Triple("Microsoft", R.drawable.ic_launcher_foreground){/*TODO*/},
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            items.forEach { (title,res,onClick) ->
                val painter = painterResource(res)
                CircularBackgroundIconButton(
                    painter = painter,
                    contentDescription = "Login with $title",
                    onClick = onClick
                )
            }
        }
    )
}

@Composable
private fun ForgotPassword(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope) {
    var dialogState by remember { mutableStateOf(false) }

    ClickableText(
        modifier = modifier,
        text = AnnotatedString("Forgot Password"),
        style = MaterialTheme.typography.titleMedium,
        onClick = { dialogState = true }
    )

    if(!dialogState) return

    val state = remember { EmailState() }
    val errorSnackbar = LocalErrorSnackbarState.current

    AlertDialog(
        onDismissRequest = { dialogState = false },
        title = { Text(text = "Email for Reset") },
        text = { EmailOutlinedTextField(state = state) },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        if(!state.isValid) {
                            state.showInvalidSnackbar(errorSnackbar)
                            return@launch
                        }

                        Firebase.auth.sendPasswordResetEmail(state.value).await()
                    }
                },
                content = {
                    Text("Send")
                }
            )
        },
    )
}

@Composable
private fun ToggleScreens(state: LoginState) {
    Row {
        Text(text = if(state.isLoginShown) "Already have an account?" else "Don't have an account?")

        Spacer(modifier = Modifier.width(8.dp))

        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(state.screenTitle)
                }
            },
            onClick = {
                state.isLoginShown = !state.isLoginShown
            }
        )
    }
}