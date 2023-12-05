package com.deathsdoor.chillback.ui.components.statistics

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatisticsGroup(
    modifier : Modifier = Modifier,
    innerModifier : Modifier = Modifier,
    // TODO : Use Lambda for stat so lazy-loading for stat can be supported and also cache in memory the stats maybe idk
    items : List<Triple<ImageVector,String,String>>,
) {
    // TODO : Allow this to be dynamically changes based on orientation and screen size
    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 2,
        content = {
            // TODO : Maybe allow for show-more / hide stats
            items.forEach { (imageVector,title,statistic) ->
                StatisticsItem(
                    modifier = innerModifier,
                    imageVector = imageVector,
                    title = title,
                    statistic = statistic
                )
            }
        }
    )
}