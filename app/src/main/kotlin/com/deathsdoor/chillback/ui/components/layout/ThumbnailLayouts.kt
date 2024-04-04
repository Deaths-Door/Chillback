package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import com.deathsdoor.chillback.ui.components.action.SelectedIcon
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize
import com.dragselectcompose.core.DragSelectState

private fun Boolean?.nonNullEnabledForIsSelected() = if(this == null) true else !this

@Deprecated("Do not use")
@Composable
@NonRestartableComposable
fun Thumbnail(
    modifier : Modifier = Modifier,
    title : String,
    textStyle : TextStyle = MaterialTheme.typography.bodyMedium,
    artwork : @Composable () -> Unit,
    subtitle : (@Composable ColumnScope.() -> Unit)? = null,
    trailingIcon : (@Composable RowScope.() -> Unit)? = null
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    artwork()

    Spacer(modifier = Modifier.width(16.dp))

    val text = @Composable {
        Text(
            modifier = Modifier
                .basicMarquee()
                .weight(1f),
            text = title,
            style = textStyle,
        )
    }

    if(subtitle == null) text() else Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        content = {
            text()
            subtitle()
        }
    )

    trailingIcon?.let { it() }
}

@Composable
fun<T> Modifier.applyToggleableOnSelection(
    item : T,
    isSelected: Boolean?,
    draggableState : DragSelectState<T>,
) = isSelected?.let {
    this then Modifier.toggleable(
        value = isSelected,
        interactionSource = remember { MutableInteractionSource() },
        indication = null, // do not show a ripple
        onValueChange = {
            if (it) draggableState.addSelected(item)
            else draggableState.removeSelected(item)
        }
    )
} ?: this

@Composable
fun SelectableThumbnail(
    modifier : Modifier = Modifier,
    isSelected : Boolean?,
    title : @Composable () -> Unit,
    artwork : @Composable () -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable RowScope.(Modifier,enabled : Boolean) -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) = Thumbnail(
    modifier = modifier,
    title = title,
    artwork = artwork,
    caption = caption,
    actionIcon = when(LocalWindowAdaptiveSize.current.widthSizeClass == WindowWidthSizeClass.Expanded) {
        true -> {{
            val weight = Modifier.weight(0.5f)

            Spacer(modifier = weight)

            actionIcon?.invoke(this,Modifier,isSelected.nonNullEnabledForIsSelected())

            Spacer(modifier = weight)

            AnimatedContent(
                modifier = Modifier.padding(end = 24.dp),
                targetState = isSelected,
                transitionSpec = {
                    (slideInHorizontally(initialOffsetX = { w -> w }) + expandHorizontally(expandFrom = Alignment.End)) togetherWith
                            (slideOutHorizontally(targetOffsetX = { w -> w }) + shrinkHorizontally(shrinkTowards = Alignment.End))
                },
                content = {
                    when(it) {
                        null -> trailingIcon?.invoke()
                        else -> SelectedIcon(isSelected = it)
                    }
                }
            )
        }}
        false -> if (isSelected == null) {{ actionIcon?.let { it(Modifier.padding(end = 12.dp), true) } }}
        else {{ SelectedIcon(modifier = Modifier.padding(end = 24.dp), isSelected = isSelected) }}
    }
)

@Composable
fun SelectableThumbnailCard(
    modifier : Modifier = Modifier,
    isSelected : Boolean?,
    title : @Composable () -> Unit,
    artwork : @Composable BoxScope.() -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable BoxScope.(Modifier,enabled : Boolean) -> Unit)? = null
) = ThumbnailCard(
    modifier = modifier,
    title = title,
    artwork = artwork,
    caption = caption,
    actionIcon = {actionModifier ->
        // enabled == !isSelected
        actionIcon?.let { it(actionModifier,isSelected.nonNullEnabledForIsSelected()) }

        isSelected?.let {
            val paddingDp = 12.dp
            SelectedIcon(modifier= Modifier.padding(top = paddingDp,start = paddingDp),isSelected = isSelected)
        }
    }
)

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