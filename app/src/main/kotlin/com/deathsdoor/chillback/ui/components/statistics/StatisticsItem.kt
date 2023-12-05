package com.deathsdoor.chillback.ui.components.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsItem(
    modifier : Modifier = Modifier,
    icon : @Composable () -> Unit,
    title : @Composable () -> Unit,
    statistic : @Composable () -> Unit
) = Row(
    modifier = modifier,
    content = {
        icon()
        Column {
            title()
            statistic()
        }
    }
)

@NonRestartableComposable
@Composable
fun StatisticsItem(
    modifier : Modifier = Modifier,
    imageVector : ImageVector,
    title : String,
    statistic : String
) = StatisticsItem(
    modifier = modifier,
    title = { Text(text = title,style = MaterialTheme.typography.labelMedium) },
    statistic = { Text(text = statistic, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge) },
    icon = { Icon(modifier = Modifier.size(42.dp).padding(end = 8.dp),imageVector = imageVector,contentDescription = null) }
)
