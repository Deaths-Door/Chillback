package com.deathsdoor.chillback.ui.components.layout

import android.net.Uri
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.components.action.SelectedIcon
import com.deathsdoor.chillback.ui.components.track.Artwork

@Composable
@NonRestartableComposable
fun ThumbnailWithText(
    modifier : Modifier = Modifier,
    artworkModifier: Modifier = Modifier,
    uri : Uri?,
    title : String,
    subtitle : String? = null,
    trailingIcon : (@Composable RowScope.() -> Unit)? = null
) = Thumbnail(
    modifier = modifier,
    title = title,
    trailingIcon = trailingIcon,
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

private fun Boolean?.nonNullEnabledForIsSelected() = if(this == null) true else !this

@Composable
fun AdaptiveGridThumbnail(
    modifier : Modifier = Modifier,
    title : String,
    subtitle : String?,
    uri : Uri?,
    isSingleItemPerRow : Boolean,
    selectedIds : MutableState<Set<Long>?>,
    isSelected : Boolean?,
    id : () -> Long,
    actionIcon : (@Composable (modifier : Modifier,enabled : Boolean) -> Unit)? = null,
) {
    @Suppress("NAME_SHADOWING")
    val modifier = isSelected?.let {
        modifier then Modifier.toggleable(
            value = isSelected,
            interactionSource = remember { MutableInteractionSource() },
            indication = null, // do not show a ripple
            onValueChange = {
                val newID = id()
                if (it) selectedIds.value = selectedIds.value!! + newID
                else selectedIds.value = selectedIds.value!! - newID
            }
        )
    } ?: modifier

    if(isSingleItemPerRow){
        ThumbnailWithText(
            modifier = modifier.fillMaxWidth(),
            artworkModifier = Modifier.size(64.dp),
            uri = uri,
            title = title,
            subtitle = subtitle,
            trailingIcon = if(isSelected == null) actionIcon?.let { { it(Modifier.padding(end = 12.dp),true) } } else {{
                SelectedIcon(modifier = Modifier.padding(end = 24.dp),isSelected = isSelected)
            }}
        )
    } else {
        Card(modifier = modifier.aspectRatio(1f)){
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Artwork(
                    modifier= Modifier.matchParentSize(),
                    uri = uri,
                    contentScale = ContentScale.FillBounds
                )

                // enabled == !isSelected
                actionIcon?.let { it(Modifier.align(Alignment.BottomEnd),isSelected.nonNullEnabledForIsSelected()) }

                isSelected?.let {
                    val paddingDp = 12.dp
                    SelectedIcon(modifier= Modifier.padding(top = paddingDp,start = paddingDp),isSelected = isSelected)
                }
            }

            val basicMarquee = Modifier.basicMarquee().padding(horizontal = 8.dp)

            // From ThumbnailLayouts.kt
            Text(
                modifier = basicMarquee,
                text = title,
                style = MaterialTheme.typography.bodyMedium,
            )

            // From ThumbnailLayouts.kt
            subtitle?.let {
                Text(
                    modifier = basicMarquee,
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@NonRestartableComposable
fun Thumbnail(
    modifier : Modifier = Modifier,
    title : String,
    artwork : @Composable () -> Unit,
    subtitle : (@Composable ColumnScope.() -> Unit)? = null,
    trailingIcon : (@Composable RowScope.() -> Unit)? = null
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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

    trailingIcon?.let { it() }
}