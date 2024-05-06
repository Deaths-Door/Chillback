package com.deathsdoor.chillback.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.newsletter_join_reason
import chillback.`feature-welcome`.generated.resources.newsletter_stay_in_loop
import chillback.`feature-welcome`.generated.resources.subscribe
import chillback.`feature-welcome`.generated.resources.your_email
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import com.deathsdoor.chillback.welcome.components.icons.OragamiBird
import com.deathsdoor.chillback.welcome.extensions.matchesEmail
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun SubscribeToNewsLetter() = Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = LocalContentColor.current
    ),
    content = {
        Box {
            Image(
                modifier = Modifier.align(Alignment.TopEnd),
                imageVector = Icons.OragamiBird,
                contentDescription = null
            )

            val windowSize = LocalWindowSize.current

            val content = @Composable { textFieldModifier : Modifier ->
                Column {
                    Text(
                        text = stringResource(Res.string.newsletter_stay_in_loop),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall,
                    )

                    Text(
                        text = stringResource(Res.string.newsletter_join_reason),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                Spacer(modifier = Modifier.size(width = 64.dp,height = 16.dp))

                // TODO : Merge this with the email textfield for authentication
                var email by remember { mutableStateOf("") }
                val isError by remember(email) { mutableStateOf(email.isNotEmpty() && !email.matchesEmail()) }

                val inverseTheme = MaterialTheme.colorScheme.inverseSurface

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    isError = isError,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = inverseTheme,
                        focusedBorderColor = inverseTheme,
                    ),
                    placeholder = { Text(stringResource(Res.string.your_email)) },
                )

                Spacer(modifier= Modifier.width(16.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = inverseTheme,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RectangleShape,
                    content = {
                        val style = LocalTextStyle.current
                        Text(
                            text = stringResource(Res.string.subscribe),
                            style = style.copy(fontSize = style.fontSize * 1.5)
                        )
                    },
                    onClick = {
                        // TODO : This feature will be implemented when this is merged with the main project , and if email is error inform user 2
                    }
                )
            }

            when(windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                true -> Row(
                    modifier = Modifier.padding(horizontal = 48.dp,vertical = 64.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        content(Modifier.weight(1f))
                    }
                )
                false -> Column(
                    modifier = Modifier.padding(horizontal = 48.dp,vertical = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        content(Modifier.fillMaxWidth())
                    }
                )
            }
        }
    }
)
