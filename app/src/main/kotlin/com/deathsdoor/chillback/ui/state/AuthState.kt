package com.deathsdoor.chillback.ui.state

import StackedSnackbarDuration
import StackedSnakbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.AuthResult

class AuthState {
    var isLoginShown by mutableStateOf(true)
    val screenTitle by derivedStateOf { if(isLoginShown) "Log-in" else "Sign-in" }

    var email by mutableStateOf("")
        private set
    var isEmailValid by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set

    // yes or no and the message with it
    var isPasswordStrongEnough : String? by mutableStateOf(null)
        private set

    fun updateEmail(value : String) {
        email = value
        isEmailValid = value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    fun updatePassword(value: String){
        password = value

        val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')
        isPasswordStrongEnough = when {
            password.length < 8 -> "Password must be at least 8 characters long."
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter."
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter."
            !password.any { it.isDigit() } ->  "Password must contain at least one digit."
            specialCharacters.none { it in password } ->  "Password must contain at least one special character (!, @, #, $, %, ^, &, *)."
            else -> null
        }
    }

    fun showInvalidSnackbar(snackbar : StackedSnakbarHostState) = snackbar.showErrorSnackbar(
        title = "Invalid Email",  // More descriptive title
        description = "The email address you entered is not valid. Please enter a valid email address and try again.",
        duration = StackedSnackbarDuration.Short
    )

    fun showEmailInUseSnackbar(snackbar: StackedSnakbarHostState) = snackbar.showErrorSnackbar(
        title = "Email Already In Use",  // Clear and informative title
        description = "The email address you entered is already associated with an account. Please try logging in or use a different email address.",
        duration = StackedSnackbarDuration.Short
    )

    fun showSuccessAuth(snackbar: StackedSnakbarHostState,authResult: AuthResult) = snackbar.showSuccessSnackbar(
        title = if(authResult.additionalUserInfo?.isNewUser == true) "Log-in Successful!"
        else "Welcome back ${authResult.user!!.displayName}!",
        duration = StackedSnackbarDuration.Short
    )
}