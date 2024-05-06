package com.deathsdoor.chillback.ui.components.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.state.AuthState

@Composable
fun AuthState.ToggleAuthMode(
    modifier : Modifier = Modifier,
    style : TextStyle= LocalTextStyle.current,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = if(isLoginShown) "Already have an account?" else "Don't have an account?",
            style = style
        )

        Spacer(modifier = Modifier.width(8.dp))

        ClickableText(
            style = style,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(screenTitle)
                }
            },
            onClick = { isLoginShown = !isLoginShown }
        )
    }
}