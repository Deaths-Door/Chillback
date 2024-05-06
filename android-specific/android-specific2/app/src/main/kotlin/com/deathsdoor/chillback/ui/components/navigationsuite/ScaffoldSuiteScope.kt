package com.deathsdoor.chillback.ui.components.navigationsuite

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.ui.Modifier

class ScaffoldSuiteScope {
    internal var header : (@Composable (isDesktop : Boolean) -> Unit)? = null
    internal val navigationItems : MutableVector<NavigationItem> = mutableVectorOf()
    internal val floatingActionButtonItems : MutableVector<FloatingActionBarItem> = mutableVectorOf()
    internal var footer : (@Composable (isDesktop : Boolean) -> Unit)? = null

    fun navigationItem(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: @Composable (() -> Unit)? = null,
        badge: (@Composable () -> Unit)? = null,
        colors: ScaffoldSuiteItemColors? = null,
        interactionSource: MutableInteractionSource? = null
    ) = navigationItems.add(
        NavigationItem(
            selected = selected,
            onClick = onClick,
            icon = icon,
            modifier = modifier,
            enabled = enabled,
            label = label,
            badge = badge,
            colors = colors,
            interactionSource = interactionSource ?: MutableInteractionSource()
        )
    )

    fun floatingActionButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        icon: @Composable (Modifier) -> Unit,
        label: @Composable (() -> Unit)?,
    ) = floatingActionButtonItems.add(
        FloatingActionBarItem(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            label = label,
        )
    )

    internal class NavigationItem(
        val selected: Boolean,
        val onClick: () -> Unit,
        val icon: @Composable () -> Unit,
        val modifier: Modifier,
        val enabled: Boolean,
        val label: @Composable (() -> Unit)?,
        val badge: (@Composable () -> Unit)?,
        val colors: ScaffoldSuiteItemColors?,
        val interactionSource: MutableInteractionSource
    )

    internal class FloatingActionBarItem(
        val onClick: () -> Unit,
        val modifier: Modifier = Modifier,
        val icon: @Composable (Modifier) -> Unit,
        val label: @Composable (() -> Unit)?,
    )
}