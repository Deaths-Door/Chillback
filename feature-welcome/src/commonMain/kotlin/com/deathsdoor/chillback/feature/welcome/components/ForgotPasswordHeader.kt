package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.core.layout.actions.BackButton
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun ForgotPasswordHeader(navController : NavController,modifier: Modifier = Modifier) = Row(modifier) {
    BackButton(modifier = Modifier.padding(end = 18.dp)) { navController.popBackStack() }

    Text(
        text = stringResource(Res.strings.password_forgot),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}