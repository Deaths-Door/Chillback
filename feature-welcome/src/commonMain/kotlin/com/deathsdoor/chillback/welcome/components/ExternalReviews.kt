@file:OptIn(ExperimentalResourceApi::class)

package com.deathsdoor.chillback.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.external_reviews_0
import chillback.`feature-welcome`.generated.resources.external_reviews_1
import chillback.`feature-welcome`.generated.resources.external_reviews_2
import chillback.`feature-welcome`.generated.resources.external_reviews_anonymous
import chillback.`feature-welcome`.generated.resources.external_reviews_caption
import chillback.`feature-welcome`.generated.resources.external_reviews_title
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ExternalReviewTitle() = Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally){
    Text(
        text = stringResource(Res.string.external_reviews_title),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayMedium,
        maxLines = 1
    )


    Text(
        modifier = Modifier.fillMaxWidth(0.7f).padding(top = 16.dp),
        text = stringResource(Res.string.external_reviews_caption),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.surfaceVariant,
        style = MaterialTheme.typography.headlineMedium,
    )
}

@Composable
internal fun ExternalReviewCards() {
    val windowSize = LocalWindowSize.current
    val arrangement = Arrangement.spacedBy(16.dp)


    val content = @Composable { modifier : Modifier ->
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

    when(windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
        true -> Row(horizontalArrangement = arrangement) {
            // TODO : Figure out how to make them all the same size
            val modifier = Modifier.weight(1f).fillMaxHeight()
            content(modifier)
        }
        false -> Column(verticalArrangement = arrangement) {
            content(Modifier.fillMaxWidth())
        }
    }

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