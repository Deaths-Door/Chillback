package com.deathsdoor.chillback.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deathsdoor.chillback.ui.components.layout.ForwardButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileCard(modifier: Modifier = Modifier,navController: NavController) = Card(
    modifier = modifier,
    onClick = { navigateToProfile(navController) },
    content = {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            content =  {
                UserProfilePicture(
                    modifier = Modifier.size(64.dp)
                        .padding(horizontal = 8.dp)
                        .clip(CircleShape)
                )

                Column {
                    Text(
                        text = Firebase.auth.currentUser!!.displayName.orEmpty().ifEmpty { "No Display Name" },
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(text = "Click to view profile")
                }

                ForwardButton(
                    contentDescription = "Go to your profile",
                    onClick = { navigateToProfile(navController) },
                )
            }
        )
    }
)

fun navigateToProfile(navController: NavController) { /*TODO : Navigate to profile , where the stats will be shown  */ }