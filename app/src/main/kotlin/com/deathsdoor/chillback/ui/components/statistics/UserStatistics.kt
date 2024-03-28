package com.deathsdoor.chillback.ui.components.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.ChillbackMaterialTheme
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview

// TODO : Get values for these statitsics instead of placehodlers
// TODO : Add more statistics
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserStatisticsCard(
    modifier : Modifier = Modifier
) = Card(modifier = modifier) {
    val section = Modifier.padding(12.dp)

    StatisticsSection(modifier = section,subtitle = "Playback Statistics") {
        StatisticsGroup(subtitle = "In Lifetime") {
            StatisticsCard(
                modifier = it,
                label = "Tracks Played",
                value = "no.. TODO",
            )

            StatisticsCard(
                modifier = it,
                label = "Time Listened",
                value = "no.. TODO",
            )
        }

        StatisticsGroup(subtitle = "In Last 7 Days") {
            StatisticsCard(
                modifier = it,
                label = "Tracks Played",
                value = "no.. TODO",
            )

            StatisticsCard(
                modifier = it,
                label = "Time Listened",
                value = "no.. TODO",
            )
        }
    }

    StatisticsSection(modifier = section,subtitle = "On Device Library") {
        StatisticsGroup(null) {
            StatisticsCard(modifier = it, label = "Tracks", value = "252")
            StatisticsCard(modifier = it,label = "Albums", value = "199")
            StatisticsCard(modifier = it,label = "Artists", value = "182")
            StatisticsCard(modifier = it,label = "Genres", value = "5")
        }
    }
}

@Composable
private fun StatisticsSection(
    modifier: Modifier = Modifier,
    subtitle : String,
    content: @Composable ColumnScope.() -> Unit
) = Column(modifier = modifier) {
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = subtitle,
        fontWeight = FontWeight.Bold,
        // headlineMedium
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
    )

    content()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.StatisticsGroup(
    subtitle : String?,
    content: @Composable FlowRowScope.(Modifier) -> Unit
) {
    val _8 = 8.dp
    subtitle?.let {
        Text(
            modifier = Modifier.padding(bottom = _8),
            text = it,
            fontWeight = FontWeight.Bold,
        // titleLarge
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
    )
    }
    
    val arrangement =  Arrangement.spacedBy(_8)

    FlowRow(
        horizontalArrangement = arrangement,
        verticalArrangement = arrangement,
        maxItemsInEachRow = 2,
        content = {
            content(Modifier.weight(1f))
        }
    )
}

@Composable
private fun StatisticsCard(
    modifier: Modifier = Modifier,
    label : String,
    value : String,
) = Card(
    modifier = modifier,
    colors = CardDefaults.elevatedCardColors(),
    elevation = CardDefaults.elevatedCardElevation(),
    content = {
        val padding = Modifier.padding(horizontal = 12.dp)

        Text(
            modifier = padding.padding(top = 16.dp),
            text = value,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )

        Text(
            modifier = padding.padding(bottom = 12.dp),
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
)



@Preview
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun UserStatisticsPreview() = InitializeProvidersForPreview {
    ChillbackMaterialTheme {
        UserStatisticsCard(modifier = Modifier)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun StatisticsCardPreview() = FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    listOf(
        "Tracks Played" to "252",
        "Listened Time" to "2h 15m",
        "Tracks Played" to "1245",
        "Listened Time" to "45h 10m",
    ).forEach { (label, value) ->
        StatisticsCard(
            modifier = Modifier,
            value = value,
            label = label
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@PreviewScreenSizes
@PreviewFontScale
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun StatisticsGroupPreview() = Column {
    StatisticsGroup(
        subtitle = "In Lifetime",
        content = {
            mapOf(
                "Tracks Played" to "252",
                "Listened Time" to "2h 15m",
                "Tracks Played" to "1245",
                "Listened Time" to "45h 10m",
            ).forEach { (label, value) ->
                StatisticsCard(
                    modifier = Modifier,
                    value = value,
                    label = label
                )
            }
        }
    )
}