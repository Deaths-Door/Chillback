package com.deathsdoor.chillback.core.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * A composable function that renders a thumbnail card with an aspect ratio of 1:1.
 *
 * @param modifier: The modifier to be applied to the card.
 * @param title: A composable function that defines the content of the title section.
 * @param artwork: A composable function that defines the content of the artwork section.
 * @param caption: An optional composable function that defines the content of the caption section.
 * @param actionIcon: An optional composable function that defines the content of an action icon displayed in the bottom-end corner.
 *
 * This function creates a card with a fixed aspect ratio of 1:1. Inside the card, it stacks the content in the following order:
 *  - Artwork
 *  - Action icon (if provided)
 *  - Title
 *  - Caption (if provided)
 *
 * The title and caption sections are wrapped in a spacer to provide some vertical spacing between the elements.
 */
@Composable
@NonRestartableComposable
fun ThumbnailCard(
    modifier : Modifier = Modifier,
    title : @Composable () -> Unit,
    artwork : @Composable BoxScope.() -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable BoxScope.(Modifier) -> Unit)? = null
) = Card(modifier = modifier.aspectRatio(1f)) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        content = {
            artwork()

            actionIcon?.invoke(
                this,
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 12.dp, end = 12.dp)
            )
        }
    )

    title()
    caption?.invoke(this)

    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * A composable function that renders a thumbnail with a horizontal layout.
 *
 * @param modifier: The modifier to be applied to the thumbnail.
 * @param title: A composable function that defines the content of the title section.
 * @param artwork: A composable function that defines the content of the artwork section.
 * @param caption: An optional composable function that defines the content of the caption section.
 * @param actionIcon: An optional composable function that defines the content of an action icon displayed on the right side.
 *
 * This function creates a row with vertical alignment set to center. It arranges the content in the following order:
 *  - Artwork
 *  - Spacer for horizontal spacing
 *  - Title (or caption section if caption is provided)
 *  - Action icon (if provided)
 *
 * If a caption is provided, it will be displayed along with the title in a separate column. Otherwise, the title will be displayed alone.
 */
@Composable
@NonRestartableComposable
fun Thumbnail(
    modifier : Modifier = Modifier,
    title : @Composable () -> Unit,
    artwork : @Composable () -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable RowScope.() -> Unit)? = null
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    artwork()

    Spacer(modifier = Modifier.width(16.dp))

    if(caption == null) title() else Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        content = {
            title()
            caption()
        }
    )

    actionIcon?.invoke(this)
}

/**
 * A composable function that renders a title text for thumbnails with basic marquee behavior.
 *
 * @param modifier: The modifier to be applied to the text.
 * @param text: The text content of the title.
 * @param style: The text style to be applied. Defaults to the current local text style.
 *
 * This function creates a text element with a basic marquee behavior to prevent text overflow. It uses the provided modifier and text style.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
@NonRestartableComposable
fun ThumbnailTitle(
    modifier : Modifier = Modifier,
    text : String,
    style : TextStyle = LocalTextStyle.current,
) = Text(
    modifier = modifier.basicMarquee(),
    text = text,
    style = style,
)

/**
 * A composable function that renders a caption text for thumbnails with basic marquee behavior and specific styling.
 *
 * @param modifier: The modifier to be applied to the text.
 * @param text: The text content of the caption.
 * @param style: The text style to be applied. Defaults to the current local text style.
 *
 * This function creates a text element with a basic marquee behavior to prevent text overflow. It uses the provided modifier, text style, and sets the text color to the surface variant color from the Material Theme color scheme.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
@NonRestartableComposable
fun ThumbnailCaption(
    modifier : Modifier = Modifier,
    text : String,
    style : TextStyle = LocalTextStyle.current,
) = Text(
    modifier = modifier.basicMarquee(),
    text = text,
    style = style,
    color = MaterialTheme.colorScheme.surfaceVariant
)