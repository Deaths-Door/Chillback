package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun ExternalReviewTitle() = Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally){
    Text(
        text = "Don't take our word for it",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayMedium,
    )

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = "See what real music lovers are saying about discovering amazing new music and creating personalized playlists.",
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.surfaceVariant,
        style = MaterialTheme.typography.headlineMedium,
    )
}

@Composable
internal fun ExternalReviewCards() = Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    // TODO : Figure out how to make them all the same size
    val modifier = Modifier.weight(1f).fillMaxHeight()

    ReviewCard(
        modifier = modifier,
        text = "I finally ditched the monthly subscription fees! This app is a game-changer for music lovers like me. It offers all the features I need to discover new music and personalize my listening experience, without the hassle of recurring payments."
    )

    ReviewCard(
        modifier = modifier,
        text = "Forget the limitations of other music apps! This one lets me create custom playlists, adjust the sound exactly how I like it, and even set custom sleep timers. It's like having my own personal music haven, and it's totally free!"
    )

    ReviewCard(
        modifier = modifier,
        text = "Forget the compromises of other apps. Here, I have complete control over the sound quality. The customizable EQ lets me fine-tune the listening experience to my exact preferences. And the best part? It's all absolutely free!"
    )
}

@Composable
private fun ReviewCard(
    modifier: Modifier,
    text : String
) = Card(
    modifier = modifier,
    colors = CardDefaults.elevatedCardColors(),
    elevation = CardDefaults.elevatedCardElevation(),
    content = {
        val padding = Modifier.padding(16.dp)
        Row(modifier = padding,verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(48.dp).padding(end = 16.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )

            Text(
                text = "By Anonymous",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Text(
            modifier = padding,
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
)