package com.deathsdoor.chillback.ui.state

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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
    var isPasswordStrongEnough: Pair<Boolean, String?> by mutableStateOf(false to null)
        private set

    fun updateEmail(value : String) {
        email = value
        isEmailValid = value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    fun updatePassword(value: String){
        password = value

        val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')
        isPasswordStrongEnough = when {
            password.length < 8 -> true to "Password must be at least 8 characters long."
            !password.any { it.isUpperCase() } -> true to "Password must contain at least one uppercase letter."
            !password.any { it.isLowerCase() } ->true to  "Password must contain at least one lowercase letter."
            !password.any { it.isDigit() } -> true to "Password must contain at least one digit."
            specialCharacters.none { it in password } -> true to "Password must contain at least one special character (!, @, #, $, %, ^, &, *)."
            else -> false to null
        }
    }

    suspend fun showInvalidSnackbar(snackbar : SnackbarHostState) = snackbar.showSnackbar("Email is not valid")
    suspend fun showEmailInUseSnackbar(snackbar: SnackbarHostState) = snackbar.showSnackbar("Sorry, email is already in use")
}