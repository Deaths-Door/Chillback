package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.DialogHost
import com.deathsdoor.chillback.core.layout.actions.BackButton
import com.deathsdoor.chillback.feature.welcome.components.EmailOutlinedTextField
import com.deathsdoor.chillback.feature.welcome.components.ForgotPasswordHeader
import com.deathsdoor.chillback.feature.welcome.components.SendPasswordResetEmail
import com.deathsdoor.chillback.feature.welcome.extensions.rememberEmailState
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun ForgotPasswordScreenMobilePortrait(navController: NavController) = Column(Modifier.padding(horizontal = 16.dp)) {
    Spacer(modifier = Modifier.fillMaxHeight(0.05f))

    ForgotPasswordHeader(navController)

    Spacer(modifier = Modifier.fillMaxHeight(0.08875f))

    Image(
        modifier = Modifier.padding(horizontal = 32.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        painter = painterResource(Res.images.forgot_password),
        contentDescription = null
    )

    val height_06125f = Modifier.fillMaxHeight(0.06125f)

    Spacer(modifier = height_06125f)

    Text(
        text = stringResource(Res.strings.enter_account_email),
        style = MaterialTheme.typography.bodyLarge
    )

    Spacer(modifier = height_06125f)

    val emailState = rememberEmailState()

    EmailOutlinedTextField(state = emailState)

    Spacer(modifier = Modifier.height(32.dp))

    SendPasswordResetEmail(navController,emailState)
}

@Composable
internal fun ForgotPasswordScreenMobileLandscape(navController: NavController) = Row(modifier = Modifier.padding(start = 16.dp,top = 24.dp,end = 40.dp,bottom = 40.dp)){
    BackButton { navController.popBackStack() }

    Column(modifier = Modifier.padding(start = 12.dp)) {
        val height_32 = Modifier.height(32.dp)

        Text(
            text = stringResource(Res.strings.password_forgot),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = height_32)

        Text(
            text = stringResource(Res.strings.enter_account_email),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = height_32)

        val emailState = rememberEmailState()

        EmailOutlinedTextField(state = emailState)

        Spacer(modifier = height_32)

        SendPasswordResetEmail(navController,emailState)
    }

    Spacer(modifier = Modifier.fillMaxWidth(0.1f))

    Image(
        modifier = Modifier.aspectRatio(1f)
            .fillMaxWidth(),
        painter = painterResource(Res.images.forgot_password),
        contentDescription = null
    )
}

@Composable
internal fun ForgotPasswordScreenDialog(onDismissRequest : () -> Unit) {
    val state = rememberEmailState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(Res.strings.password_forgot),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.Bottom) {
                Image(
                    modifier = Modifier.aspectRatio(1f)
                        .fillMaxHeight(),
                    painter = painterResource(Res.images.forgot_password),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Text(
                        text = stringResource(Res.strings.enter_account_email),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    EmailOutlinedTextField(state = state)
                }
            }
        },
        confirmButton = {
            SendPasswordResetEmail(state = state,goBack = onDismissRequest)
        }
    )
}