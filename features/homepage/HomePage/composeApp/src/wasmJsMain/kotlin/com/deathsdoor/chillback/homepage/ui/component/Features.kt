package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Features() = Column {
    Text(
        text = "Features",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayMedium,
    )

    Text(
        text = "Here are some cool features offered by Chillback",
        style = MaterialTheme.typography.titleLarge,
    )


    FlowRow(
        modifier = Modifier.padding(top = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        maxItemsInEachRow = 3,
        content = {
            // TODO : Update Icons for each item and make all of them the same height
            val weight = Modifier.weight(1f) //.fillMaxItemHeight <- Not there for kmp compose??

            FeatureCard(
                modifier = weight,
                imageVector = Icons.Default.AccountCircle,
                title = "Open Source & Lightweight",
                description = "Built with transparency and efficiency in mind, the app runs smoothly without sacrificing performance"
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Modern UI & Customizable Themes",
                description = "Enjoy a sleek and intuitive interface that adapts to your style with a variety of themes to choose from.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Free Offline Playback:",
                description = "Listen to your music anytime, anywhere, without worrying about data charges. It's your music, available offline for free.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Seamless Syncing Across Devices",
                description = "ake your music with you. Keep your playlists and library in sync across all your devices for uninterrupted listening.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Free Spotify Alternative",
                description = " Ditch the subscription fees! This app offers a powerful and customizable music experience without breaking the bank.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Advanced Playback",
                description = "Fine-tune your listening experience with advanced playback controls like equalizer, gapless playback, and sleep timer.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Advanced Search & Filtering",
                description = "Find the exact music you crave with powerful search and filter options. Browse by artist, genre, year, or even mood.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Cross-Platform Compatibility",
                description = "Access your music library across all your devices, from your phone to your smartwatch and many more.",
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = "Constant Development",
                description = "We're always working to improve the app, adding new features and refining the experience based on your feedback.",
                modifier = weight
            )
        }
    )
}

@Composable
private fun FeatureCard(
    imageVector: ImageVector,
    title: String,
    description: String,
    modifier: Modifier,
) = Card(
    modifier = modifier,
    colors = CardDefaults.elevatedCardColors(),
    elevation = CardDefaults.elevatedCardElevation(),
    content = {
        val padding = Modifier.padding(horizontal = 24.dp,vertical = 16.dp)

        Row(modifier = padding, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = imageVector,
                contentDescription = null
            )

            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Text(
            modifier = padding,
            text = description
        )
    }
)