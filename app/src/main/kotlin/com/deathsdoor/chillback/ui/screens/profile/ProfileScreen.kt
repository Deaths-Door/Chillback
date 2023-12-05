package com.deathsdoor.chillback.ui.screens.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.ui.components.profile.UserProfileCard
import com.deathsdoor.chillback.ui.components.profile.UserProfileStatistics

// TODO : Finsih this screen from music app on phone and posh it up
@Composable
fun ProfileScreen() = LazyColumn(modifier = Modifier.padding(16.dp)) {
    item { UserProfileCard(modifier = Modifier.fillMaxWidth().padding(8.dp)) }
    item { UserProfileStatistics(modifier = Modifier.padding(vertical = 16.dp)) }
    item { ThemeWidgetSection() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeWidgetSection() = Row {
    listOf(
        "Themes" to "theme",
        "Widgets" to "widget"
    ).forEach { (text, route) ->
        // For some reason it works inside a card but not without it??
        Card(
            modifier = Modifier.weight(1f).height(80.dp).padding(end = 12.dp),
            onClick = { /*TODO : Navigate to $route-selection section*/ },
            content = {
                BackgroundImage(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    content = {
                        Text(
                            modifier = Modifier.padding(start = 8.dp,top = 16.dp),
                            text = text,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        )
    }
}