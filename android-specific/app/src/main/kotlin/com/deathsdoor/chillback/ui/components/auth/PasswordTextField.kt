package com.deathsdoor.chillback.ui.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.deathsdoor.chillback.ui.state.AuthState

@Composable
fun AuthState.PasswordOutlinedTextField(modifier: Modifier = Modifier,style : TextStyle)= OutlinedTextField(
    modifier = modifier,
    value = password,
    onValueChange = { updatePassword(it) },
    label = { Text(text = "Password",style = style) },
    leadingIcon = {
        Image(
            imageVector = Icons.Default.Edit,
            contentDescription = null
        )
    } ,
    visualTransformation = PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    textStyle = style,
    isError = password.isNotEmpty() && isPasswordStrongEnough != null ,
    singleLine = true
)