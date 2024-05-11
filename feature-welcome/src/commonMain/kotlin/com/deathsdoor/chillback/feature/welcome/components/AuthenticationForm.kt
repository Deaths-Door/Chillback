package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.LocalWindowSize
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.feature.welcome.extensions.rememberEmailState
import com.deathsdoor.chillback.feature.welcome.extensions.rememberPasswordState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun AuthenticationForm(
    modifier: Modifier = Modifier,
    isLoginScreen : MutableState<Boolean>,
    coroutineScope : CoroutineScope,
    navigateToForgotPassword : () -> Unit,
    content : @Composable () -> Unit
) = Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    val emailState = rememberEmailState()
    val passwordState = rememberPasswordState()

    val maxWidth = Modifier.fillMaxWidth()

    EmailOutlinedTextField(modifier = maxWidth,state = emailState)

    val _16 = 16.dp

    PasswordOutlinedTextField(
        modifier = maxWidth.padding(top = 32.dp,bottom = _16),
        state = passwordState
    )

    if(!isLoginScreen.value && LocalWindowSize.current.widthSizeClass == WindowWidthSizeClass.Expanded) {
        TermsAndPolicyDisclaimer(
            imageModifier = Modifier.size(48.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }

    ClickableText(
        modifier = Modifier.padding(bottom= _16),
        text = Res.strings.password_forgot.styleWith(),
        style = MaterialTheme.typography.bodyMedium.copy(textDecoration  = TextDecoration.Underline),
        onClick = { navigateToForgotPassword() }
    )

    AuthenticationButton(
        isLoginScreen = isLoginScreen.value,
        emailState = emailState,
        passwordState = passwordState,
        coroutineScope = coroutineScope,
    )

    content()

    ToggleAuthMode(isLoginScreen = isLoginScreen)
}