package com.deathsdoor.chillback.feature.welcome.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.deathsdoor.chillback.core.layout.LazyResourceLoader
import com.deathsdoor.chillback.core.layout.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.core.layout.stringResource
import com.deathsdoor.chillback.features.welcome.resources.Res

internal class EmailState {
    var isValid by mutableStateOf(false)
        private set
    var email by mutableStateOf("")
        private set

    fun update(value : String) {
        email = value
        isValid = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    fun informUserOnInvalidEmail(snackBarState : StackableSnackbarState,lazyResourceLoader: LazyResourceLoader,onSuccess : () -> Unit) {
        if(isValid) onSuccess()
        else snackBarState.showErrorSnackbar(
            title = lazyResourceLoader.stringResource(Res.strings.invalid_email),
            description = lazyResourceLoader.stringResource(Res.strings.invalid_email_description)
        )
    }
}