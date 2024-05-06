package com.deathsdoor.chillback.welcome.components

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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.features_advanced_playback_description
import chillback.`feature-welcome`.generated.resources.features_advanced_playback_title
import chillback.`feature-welcome`.generated.resources.features_advanced_search_filtering_description
import chillback.`feature-welcome`.generated.resources.features_advanced_search_filtering_title
import chillback.`feature-welcome`.generated.resources.features_caption
import chillback.`feature-welcome`.generated.resources.features_constant_development_description
import chillback.`feature-welcome`.generated.resources.features_constant_development_title
import chillback.`feature-welcome`.generated.resources.features_cross_platform_compatibility_description
import chillback.`feature-welcome`.generated.resources.features_cross_platform_compatibility_title
import chillback.`feature-welcome`.generated.resources.features_free_offline_playback_description
import chillback.`feature-welcome`.generated.resources.features_free_offline_playback_title
import chillback.`feature-welcome`.generated.resources.features_free_spotify_alternative_description
import chillback.`feature-welcome`.generated.resources.features_free_spotify_alternative_title
import chillback.`feature-welcome`.generated.resources.features_modern_ui_description
import chillback.`feature-welcome`.generated.resources.features_modern_ui_title
import chillback.`feature-welcome`.generated.resources.features_seamless_syncing_description
import chillback.`feature-welcome`.generated.resources.features_seamless_syncing_title
import chillback.`feature-welcome`.generated.resources.features_title
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import com.deathsdoor.chillback.welcome.components.icons.InDevelopment

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

    val windowSize = LocalWindowSize.current

    val maxItemsInEachRow = when(windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
        true -> 3
        false -> 1
    }

    FlowRow(
        modifier = Modifier.padding(top = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        maxItemsInEachRow = maxItemsInEachRow,
        content = {
            // TODO : Update Icons for each item and make all of them the same height
            val weight = Modifier.weight(1f) //.fillMaxItemHeight <- Not there for kmp compose??

            // TODO : Enable this again
            /*FeatureCard(
                imageVector = Icons.OpenSource,
                title = stringResource(Res.string.features_open_source_title),
                description = stringResource(Res.string.features_open_source_description),
                modifier = weight
            )*/

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