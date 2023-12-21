package com.deathsdoor.chillback.ui.components.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.components.statistics.StatisticsGroup

@Composable
fun UserProfileStatistics(modifier: Modifier = Modifier) {
    // TODO : Replace this with actual statistics
    val items = listOf(
        Triple(Icons.Default.Email,"Music Played","725 times"),
        Triple(Icons.Default.Email,"Listening Time","1546 mins"),
        Triple(Icons.Default.Email,"Favourite Genre","Pop"),
        Triple(Icons.Default.Email,"Local Song Count","500 songs"),
    )

    // TODO : Add show more button
    StatisticsGroup(
        modifier = modifier,
        innerModifier = Modifier.fillMaxWidth(0.5f).padding(bottom = 16.dp),
        items = items,
    )
}