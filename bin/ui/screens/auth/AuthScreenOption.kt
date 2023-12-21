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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage


@Composable
fun AuthScreenOption(navController: NavController) {
    var isLoginShown by remember { mutableStateOf(true) }
    val screenTitle by remember(isLoginShown) { derivedStateOf{ if (isLoginShown) "Login" else "Sign-In" } }

    BackgroundImage(
        // TODO : Visual issue will be fixed when correct image is given
        painter = painterResource(R.drawable.ic_launcher_background),
        content = {
            BackButton { navController.popBackStack() }

            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(
                        modifier = Modifier.padding(top = 36.dp),
                        text = "Let's get you in",
                        style = MaterialTheme.typography.headlineLarge,
                    )

                    // 36 + 16 + 64 = x + 44 to offset not text there (text height)
                    var height = 100
                    if(isLoginShown){
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "Please sign-in to continue",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )

                    } else { height += 44 }

                    Spacer(modifier = Modifier.height(height.dp))

                    var email : String? = null
                    EmailOutlinedTextField(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onValidEmailSet = { email = it }
                    )

                    var password : String? = null
                    PasswordOutlinedTextField(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onStrongEnoughPasswordSet = { password = it }
                    )

                    AuthButton(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        screenTitle = screenTitle,
                        email = email,
                        password = password
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if(isLoginShown) {
                        ForgotPassword()
                        Divider(modifier = Modifier.padding(8.dp))
                        AlternativeAuthOptions(modifier = Modifier.fillMaxWidth().padding(top = 8.dp,bottom = 16.dp))
                    }

                    ToggleScreens(
                        isLoginShown = isLoginShown,
                        screenTitle = screenTitle,
                        onClick = {
                            isLoginShown = !isLoginShown
                        }
                    )
                }
            )
        }
    )

}

// TODO : Maybe convert these into components so they could be reused by other parts of the code eg settings mostly`

@Composable
private fun ToggleScreens(
    isLoginShown: Boolean,
    screenTitle: String,
    onClick : (Int) -> Unit
) {
    Row {
        Text(text = if(isLoginShown) "Already have an account?" else "Don't have an account?")

        Spacer(modifier = Modifier.width(8.dp))

        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(screenTitle)
                }
            },
            onClick = onClick
        )
    }
}

@Composable
private fun EmailOutlinedTextField(
    modifier: Modifier = Modifier,
    onValidEmailSet : (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = email,
        onValueChange = {
            email = it
            isValidEmail = email.matches( "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())

            if(isValidEmail) {
                onValidEmailSet(it)
            }
        },
        label = { Text(text = "Email") },
        leadingIcon = {
            Image(
                imageVector = Icons.Default.Email,
                contentDescription = null
            )
        } ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = email.isNotEmpty() && !isValidEmail,
        singleLine = true
    )
}


fun passwordStrength(password: String): String? {
    // Check minimum length
    if (password.length < 8) {
        return "Password must be at least 8 characters long."
    }

    // Check for at least one uppercase letter
    if (!password.any { it.isUpperCase() }) {
        return "Password must contain at least one uppercase letter."
    }

    // Check for at least one lowercase letter
    if (!password.any { it.isLowerCase() }) {
        return "Password must contain at least one lowercase letter."
    }

    // Check for at least one digit
    if (!password.any { it.isDigit() }) {
        return "Password must contain at least one digit."
    }

    // Check for at least one special character
    val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')
    if (!password.any { specialCharacters.contains(it) }) {
        return "Password must contain at least one special character (!, @, #, $, %, ^, &, *)."
    }

    // Password meets all criteria
    return null
}

@Composable
private fun PasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    onStrongEnoughPasswordSet : (String) -> Unit
){
    var password by remember { mutableStateOf("") }
    var isPasswordStrongEnough by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = {
            password = it
            // TODO : Call passwordStrength and display message
            //isPasswordStrongEnough = passwordStrength(password)

            if (isPasswordStrongEnough) {
                onStrongEnoughPasswordSet(password)
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
        isError = password.isNotEmpty() && !isPasswordStrongEnough,
        singleLine = true
    )
}

@Composable
private fun AuthButton(
    modifier : Modifier = Modifier,
    screenTitle : String,
    email : String?,
    password : String?
) {
    Button(
        modifier = modifier,
        content = {
            Text(
                text = screenTitle,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onClick = { /*TODO : Login/Sign-up using creds */ }
    )
}


@Composable
private fun ForgotPassword() {
    ClickableText(
        text = buildAnnotatedString {
            append("Forgot Password")
        },
        style = MaterialTheme.typography.titleMedium,
        onClick = { /* TODO */ }
    )
}

@Composable
private fun AlternativeAuthOptions(modifier : Modifier = Modifier) {
    val items = listOf(
        Triple("Google",R.drawable.ic_launcher_foreground){ /*TODO*/ },
        Triple("Apple",R.drawable.ic_launcher_foreground){ /*TODO*/},
        Triple("Microsoft",R.drawable.ic_launcher_foreground){/*TODO*/},
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            items.forEach {
                val painter = painterResource(it.second)
                IconButton(
                    // TODO : Change background to black for dark mode
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        .clip(CircleShape),
                    content = {
                        Icon(painter = painter,contentDescription = "Login with ${it.first}")
                    },
                    onClick = it.third
                )
            }
        }
    )
}