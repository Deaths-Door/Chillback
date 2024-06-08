package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun ToggleAuthMode(
    isLoginScreen : MutableState<Boolean>,
    modifier : Modifier = Modifier
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    val (title,accountStatus) = when(isLoginScreen.value) {
        true -> Res.strings.signin to Res.strings.have_account
        false -> Res.strings.login to Res.strings.do_not_have_account
    }

    Text(
        text = stringResource(accountStatus)
    )

    Spacer(modifier = Modifier.width(8.dp))

    ClickableText(
        text = title.styleWith(spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)),
        onClick = { isLoginScreen.value = !isLoginScreen.value }
    )
}