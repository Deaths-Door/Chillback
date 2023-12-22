package com.deathsdoor.chillback.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
;
@Composable
fun SplashScreen(onFinish : @Composable () -> Unit) {
    // TODO : Make this full screen do it so by default in fulscreen and then based on settings change it
    var isFinished by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1500)
        isFinished = true
    }

    if (isFinished) {
        onFinish()
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Image(
                modifier = Modifier.fillMaxSize(0.25f),
                imageVector = Icons.Default.Email,
                contentDescription = null
            )

            Text(text = "Chillback", style = MaterialTheme.typography.displayMedium)

            Text(
                text = "Without Music \nLife would be a mistake",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}
