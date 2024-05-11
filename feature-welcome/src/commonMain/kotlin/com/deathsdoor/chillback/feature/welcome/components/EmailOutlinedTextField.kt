package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.feature.welcome.states.EmailState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun EmailOutlinedTextField(
    state : EmailState,
    modifier: Modifier = Modifier
) = OutlinedTextField(
    modifier = modifier,
    value = state.email,
    onValueChange = { state.update(it) },
    label = { Text(text = stringResource(Res.strings.email)) },
    placeholder = { Text("example@gmail.com") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    isError = state.email.isNotEmpty() && !state.isValid,
    singleLine = true
)