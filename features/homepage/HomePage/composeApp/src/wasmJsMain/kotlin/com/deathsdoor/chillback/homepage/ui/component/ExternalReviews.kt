@file:OptIn(ExperimentalResourceApi::class)

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
import homepage.composeapp.generated.resources.Res
import homepage.composeapp.generated.resources.external_reviews_0
import homepage.composeapp.generated.resources.external_reviews_1
import homepage.composeapp.generated.resources.external_reviews_2
import homepage.composeapp.generated.resources.external_reviews_anonymous
import homepage.composeapp.generated.resources.external_reviews_caption
import homepage.composeapp.generated.resources.external_reviews_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExternalReviewTitle() = Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally){
    Text(
        text = stringResource(Res.string.external_reviews_title),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayMedium,
    )

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(Res.string.external_reviews_caption),
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
        text = stringResource(Res.string.external_reviews_0)
    )

    ReviewCard(
        modifier = modifier,
        text = stringResource(Res.string.external_reviews_1)
    )

    ReviewCard(
        modifier = modifier,
        text = stringResource(Res.string.external_reviews_2)
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
                text = stringResource(Res.string.external_reviews_anonymous),
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