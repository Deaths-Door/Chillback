package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.core.layout.actions.BackButton
import com.deathsdoor.chillback.core.layout.extensions.applyOnNotNull
import com.deathsdoor.chillback.feature.welcome.components.AuthenticationForm
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.CoroutineScope

@Suppress("UnusedReceiverParameter")
@Composable
internal fun RowScope.ChillbackAuthScreenDesktop(coroutineScope : CoroutineScope){
    Spacer(modifier =  Modifier.fillMaxWidth(0.2f))

    var isForgotPasswordShown by remember { mutableStateOf(false) }

    Box {
        Image(
            modifier = Modifier.align(Alignment.TopEnd),
            painter = painterResource(Res.images.outer_circle_filled_with_orange_gradient),
            contentDescription = null
        )

        Portrait(
            coroutineScope = coroutineScope,
            navigateToForgotPassword = { isForgotPasswordShown = true },
            content = {
                SkipButton(coroutineScope = coroutineScope)
            }
        )
    }

    if(!isForgotPasswordShown) return

    ForgotPasswordScreenDialog { isForgotPasswordShown = false }
}

@Composable
internal fun ChillbackAuthScreenMobilePortrait(
    navController: NavController,
    coroutineScope : CoroutineScope,
) {
    Box {
        var imageSize: IntSize? by remember { mutableStateOf(null) }
        Image(
            modifier = Modifier.align(Alignment.TopCenter)
                .onSizeChanged { imageSize = it }
                .applyOnNotNull(imageSize) {
                    offset(y = (-150).dp)
                },
            painter = painterResource(Res.images.outer_circle_filled_with_orange_gradient),
            contentDescription = null
        )

        BackButton { navController.popBackStack() }

        Portrait(
            coroutineScope = coroutineScope,
            navigateToForgotPassword = { navController.navigateToForgotPassword() },
        )
    }
}


@Composable
internal fun ChillbackAuthScreenMobileLandscape(
    navController: NavController,
    coroutineScope : CoroutineScope,
){
    Box {
        Image(
            modifier = Modifier.align(Alignment.TopStart),
            painter = painterResource(Res.images.outer_circle_filled_with_orange_gradient),
            contentDescription = null
        )

        Row(modifier = Modifier.fillMaxSize()) {
            val isLoginScreen = remember { mutableStateOf(true) }

            Column {
                BackButton { navController.popBackStack() }

                Welcome(
                    modifier = Modifier.padding(start = 24.dp),
                    isLoginScreen = isLoginScreen.value
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AuthenticationForm(
                isLoginScreen = isLoginScreen,
                coroutineScope = coroutineScope,
                navigateToForgotPassword = { navController.navigateToForgotPassword() },
                content = {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            )

            Spacer(modifier = Modifier.fillMaxWidth(0.11f))
        }
    }
}

@Composable
private fun Portrait(
    coroutineScope : CoroutineScope,
    navigateToForgotPassword : () -> Unit,
    content : (@Composable () -> Unit)? = null
) = Column(modifier = Modifier.fillMaxSize()) {
    Spacer(modifier = Modifier.fillMaxHeight(0.125f))

    val isLoginScreen = remember { mutableStateOf(true) }

    Welcome(
        modifier = Modifier.padding(start = 16.dp),
        isLoginScreen = isLoginScreen.value
    )

    Spacer(modifier = Modifier.fillMaxHeight(0.15125f))

    AuthenticationForm(
        modifier = Modifier.padding(horizontal = 16.dp),
        isLoginScreen = isLoginScreen,
        coroutineScope = coroutineScope,
        navigateToForgotPassword = navigateToForgotPassword,
        content = {
            // TODO : blah blah blah -> alternative providers

            content?.invoke()

            Spacer(modifier = Modifier.fillMaxHeight(0.185f))
        }
    )
}



@Composable
private fun Welcome(modifier : Modifier,isLoginScreen: Boolean) = Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
    val (title,imperative) = when(isLoginScreen) {
        true -> Res.strings.welcome_back to Res.strings.login
        false -> Res.strings.welcome_new to Res.strings.signin
    }
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = stringResource(Res.strings.welcome_common_phrase,stringResource(imperative)),
        style = MaterialTheme.typography.bodyMedium
    )
}