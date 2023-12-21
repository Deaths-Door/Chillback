package com.deathsdoor.chillback.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier

// TODO : Replace with actual profile image logic
@NonRestartableComposable
@Composable
fun UserProfilePicture(modifier : Modifier = Modifier) = Image(
    modifier = modifier,
    imageVector = Icons.Default.ArrowBack,
    contentDescription = "Profile Picture"
)