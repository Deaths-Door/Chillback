package com.deathsdoor.chillback.feature.welcome.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarDuration
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.StringResource

internal class PasswordState {
    var isPasswordStrongEnough : StringResource?  by mutableStateOf(null)
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        internal set

    fun update(value : String) {
        password = value

        isPasswordStrongEnough = when {
            password.length < 8 -> Res.strings.password_min_length
            !password.any { it.isUpperCase() } -> Res.strings.password_one_uppercase
            !password.any { it.isLowerCase() } -> Res.strings.password_one_lowercase
            !password.any { it.isDigit() } ->  Res.strings.password_one_digit
            setOf('!', '@', '#', '$', '%', '^', '&', '*').none { it in password } ->  Res.strings.password_one_special_character
            else -> null
        }
    }

    fun informUserOnWeakPassword(snackBarState : StackableSnackbarState, lazyResourceLoader: LazyResourceLoader, onSuccess : () -> Unit) {
        isPasswordStrongEnough?.let { resource ->
            snackBarState.showErrorSnackbar(
                title = lazyResourceLoader.stringResource(Res.strings.weak_password),
                description = lazyResourceLoader.stringResource(Res.strings.invalid_email_description,resource),
                duration = StackableSnackbarDuration.Long
            )
        } ?: onSuccess()
    }
}