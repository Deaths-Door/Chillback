package com.deathsdoor.chillback.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO : Use actual information for this
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileCard(modifier: Modifier) = Card(
    modifier = modifier,
    onClick = { /*TODO : Move to settings page*/ },
    content = {
        Row(modifier = Modifier.padding(12.dp)) {
            UserProfilePicture(modifier = Modifier.size(64.dp).padding(horizontal = 8.dp))

            Column {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Click to view settings",)
            }

            IconButton(
                onClick = { /*TODO : Show preview of stats and make the stats itself and share it */ },
                content = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Music Statistics"
                    )
                }
            )
        }
    }
)