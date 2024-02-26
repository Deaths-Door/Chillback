package com.deathsdoor.chillback.ui.components.layout

import android.net.Uri
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.components.track.Artwork

@Composable
@NonRestartableComposable
fun ThumbnailWithText(
    modifier : Modifier = Modifier,
    artworkModifier: Modifier = Modifier,
    uri : Uri?,
    title : String,
    subtitle : String? = null,
    leadingIcon : (@Composable RowScope.() -> Unit)? = null
) = Thumbnail(
    modifier = modifier,
    title = title,
    leadingIcon = leadingIcon,
    artwork = {
        Artwork(
            modifier = artworkModifier,
            uri = uri
        )
    },
    subtitle = if(subtitle != null) {{
        // Should only be executed when subtitle is not null
        Text(
            modifier = Modifier.basicMarquee(),
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
    }} else null
)

@Composable
fun Thumbnail(
    modifier : Modifier = Modifier,
    title : String,
    artwork : @Composable () -> Unit,
    subtitle : (@Composable ColumnScope.() -> Unit)? = null,
    leadingIcon : (@Composable RowScope.() -> Unit)? = null
) = Row(modifier = modifier) {
    artwork()

    Spacer(modifier = Modifier.width(16.dp))

    val text = @Composable {
        Text(
            modifier = Modifier.basicMarquee().weight(1f),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    if(subtitle == null) text() else Column(
        modifier = Modifier.padding(vertical = 12.dp).weight(1f),
        verticalArrangement = Arrangement.Center,
        content = {
            text()
            subtitle()
        }
    )

    leadingIcon?.let { it() }
}