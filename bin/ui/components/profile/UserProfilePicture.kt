package com.deathsdoor.chillback.ui.components.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@NonRestartableComposable
@Composable
fun UserProfilePicture(modifier : Modifier = Modifier) = AsyncImage(
    modifier = modifier,
    model = Firebase.auth.currentUser!!.photoUrl,
    placeholder = rememberVectorPainter(Icons.Default.AccountCircle),
    contentDescription = null,
    contentScale = ContentScale.Crop,
)