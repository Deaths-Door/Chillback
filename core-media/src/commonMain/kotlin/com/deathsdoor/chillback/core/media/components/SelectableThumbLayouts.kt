package com.deathsdoor.chillback.core.media.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.AdaptiveLayoutGeneric
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailCard
import com.deathsdoor.chillback.core.media.components.action.SelectedIcon

private fun Boolean?.isEnabled() = if(this == null) true else !this

@Composable
internal fun SelectableThumbnailCard(
    modifier : Modifier = Modifier,
    isSelected : Boolean?,
    elevation : Dp,
    title : @Composable () -> Unit,
    artwork : @Composable BoxScope.() -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable BoxScope.(Modifier,enabled : Boolean) -> Unit)? = null
) = ThumbnailCard(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = elevation),
    title = title,
    artwork = artwork,
    caption = caption,
    actionIcon = { actionModifier ->
        // enabled == !isSelected
        actionIcon?.let { it(actionModifier,isSelected.isEnabled()) }

        isSelected?.let {
            val paddingDp = 12.dp
            SelectedIcon(modifier= Modifier.padding(top = paddingDp,start = paddingDp),isSelected = isSelected)
        }
    }
)

@Composable
internal fun SelectableThumbnail(
    modifier : Modifier = Modifier,
    isSelected : Boolean?,
    elevation : Dp,
    title : @Composable () -> Unit,
    artwork : @Composable (size : Dp) -> Unit,
    caption : (@Composable ColumnScope.() -> Unit)? = null,
    actionIcon : (@Composable RowScope.(Modifier,enabled : Boolean) -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) = Thumbnail(
    modifier = modifier,
    elevation = elevation,
    title = title,
    caption = caption,
    artwork = AdaptiveLayoutGeneric<@Composable () -> Unit>(
        onDesktop = {{ artwork(64.dp) }},
        onMobile = {{
            AnimatedSlideInAndOut(
                targetState = isSelected,
                content = {
                    val size = 64.dp
                    when(it) {
                        null -> artwork(size)
                        else -> SelectedIcon(modifier = Modifier.size(size),isSelected = it)
                    }
                }
            )
        }}
    ),
    actionIcon = actionIcon?.let {
        AdaptiveLayoutGeneric<@Composable RowScope.() -> Unit>(
            onDesktop = {{
                it(this,Modifier,isSelected.isEnabled())

                Spacer(modifier = Modifier.padding(end = 8.dp))

                AnimatedSlideInAndOut(
                    targetState = isSelected,
                    content = {
                        when(it) {
                            null -> trailingIcon?.invoke()
                            else -> SelectedIcon(isSelected = it)
                        }
                    }
                )
            }},
            onMobile = {{ it(Modifier.padding(end = 12.dp),isSelected.isEnabled()) }}
        )
    }
)

@Composable
private fun<T> AnimatedSlideInAndOut(targetState : T?,content : @Composable  AnimatedContentScope.(T?) -> Unit) = AnimatedContent(
    modifier = Modifier.padding(end = 24.dp),
    targetState = targetState,
    transitionSpec = {
        (slideInHorizontally(initialOffsetX = { w -> w }) + expandHorizontally(expandFrom = Alignment.End)) togetherWith
                (slideOutHorizontally(targetOffsetX = { w -> w }) + shrinkHorizontally(shrinkTowards = Alignment.End))
    },
    content = content
)
