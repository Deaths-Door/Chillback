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
import com.deathsdoor.chillback.homepage.ui.component.icons.InDevelopment
import com.deathsdoor.chillback.homepage.ui.component.icons.OpenSource
import homepage.composeapp.generated.resources.Res
import homepage.composeapp.generated.resources.features_advanced_playback_description
import homepage.composeapp.generated.resources.features_advanced_playback_title
import homepage.composeapp.generated.resources.features_advanced_search_filtering_description
import homepage.composeapp.generated.resources.features_advanced_search_filtering_title
import homepage.composeapp.generated.resources.features_caption
import homepage.composeapp.generated.resources.features_constant_development_description
import homepage.composeapp.generated.resources.features_constant_development_title
import homepage.composeapp.generated.resources.features_cross_platform_compatibility_description
import homepage.composeapp.generated.resources.features_cross_platform_compatibility_title
import homepage.composeapp.generated.resources.features_free_offline_playback_description
import homepage.composeapp.generated.resources.features_free_offline_playback_title
import homepage.composeapp.generated.resources.features_free_spotify_alternative_description
import homepage.composeapp.generated.resources.features_free_spotify_alternative_title
import homepage.composeapp.generated.resources.features_modern_ui_description
import homepage.composeapp.generated.resources.features_modern_ui_title
import homepage.composeapp.generated.resources.features_open_source_description
import homepage.composeapp.generated.resources.features_open_source_title
import homepage.composeapp.generated.resources.features_seamless_syncing_description
import homepage.composeapp.generated.resources.features_seamless_syncing_title
import homepage.composeapp.generated.resources.features_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
internal fun Features() = Column {
    Text(
        text = stringResource(Res.string.features_title),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayMedium,
    )

    Text(
        text = stringResource(Res.string.features_caption),
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
                imageVector = Icons.OpenSource,
                title = stringResource(Res.string.features_open_source_title),
                description = stringResource(Res.string.features_open_source_description),
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle,
                title = stringResource(Res.string.features_modern_ui_title),
                description = stringResource(Res.string.features_modern_ui_description),
                modifier = weight
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_free_offline_playback_title),
                description = stringResource(Res.string.features_free_offline_playback_description),
                modifier = weight 
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_seamless_syncing_title),
                description = stringResource(Res.string.features_seamless_syncing_description),
                modifier = weight 
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_free_spotify_alternative_title),
                description = stringResource(Res.string.features_free_spotify_alternative_description),
                modifier = weight 
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_advanced_playback_title),
                description = stringResource(Res.string.features_advanced_playback_description),
                modifier = weight 
            )


            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_advanced_search_filtering_title),
                description = stringResource(Res.string.features_advanced_search_filtering_description),
                modifier = weight 
            )

            FeatureCard(
                imageVector = Icons.Default.AccountCircle, 
                title = stringResource(Res.string.features_cross_platform_compatibility_title),
                description = stringResource(Res.string.features_cross_platform_compatibility_description),
                modifier = weight 
            )

            FeatureCard(
                imageVector = Icons.InDevelopment,
                title = stringResource(Res.string.features_constant_development_title),
                description = stringResource(Res.string.features_constant_development_description),
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