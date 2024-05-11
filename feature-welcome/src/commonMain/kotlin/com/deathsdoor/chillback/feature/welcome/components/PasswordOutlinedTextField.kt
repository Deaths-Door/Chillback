package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.feature.welcome.icons.VisibilityOff
import com.deathsdoor.chillback.feature.welcome.icons.VisibilityOn
import com.deathsdoor.chillback.feature.welcome.states.EmailState
import com.deathsdoor.chillback.feature.welcome.states.PasswordState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource


// https://stackoverflow.com/questions/65304229/toggle-password-field-jetpack-compose
@Composable
internal fun PasswordOutlinedTextField(
    state : PasswordState,
    modifier: Modifier = Modifier
) = OutlinedTextField(
    modifier = modifier,
    visualTransformation = if(state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    isError = state.password.isNotEmpty() && state.isPasswordStrongEnough != null,
    singleLine = true,
    value = state.password,
    onValueChange = { state.update(it) },
    label = { Text(text = stringResource(Res.strings.password)) },
    trailingIcon = {
        IconButton(
            onClick = { state.isPasswordVisible = !state.isPasswordVisible },
            content = {
                val (imageVector,contentDescription) = when(state.isPasswordVisible) {
                    false -> Icons.VisibilityOff to Res.strings.password_invisible
                    true -> Icons.VisibilityOn to Res.strings.password_visible
                }

                Icon(
                    imageVector = imageVector,
                    contentDescription = stringResource(contentDescription)
                )
            }
        )
    },
)