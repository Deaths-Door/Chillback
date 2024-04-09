package com.deathsdoor.chillback.ui.components.auth

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.state.AuthState

@Composable
fun AuthState.EmailOutlinedTextField(
    modifier: Modifier = Modifier,
    style: TextStyle
)= OutlinedTextField(
    modifier = modifier,
    value = email,
    onValueChange = { updateEmail(it) },
    textStyle = style,
    label = { Text(text = "Email",style = style) },
    placeholder = { Text("example@gmail.com",style = style) },
    leadingIcon = {
        Image(
            modifier = Modifier.size(48.dp).padding(horizontal = 8.dp),
            imageVector = Icons.Default.Email,
            contentDescription = null
        )
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    isError = email.isNotEmpty() && !isEmailValid,
    singleLine = true
)