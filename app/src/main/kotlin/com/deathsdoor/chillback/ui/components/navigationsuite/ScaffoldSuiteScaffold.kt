package com.deathsdoor.chillback.ui.components.navigationsuite

import StackedSnackbarHost
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize

@Composable
fun ScaffoldSuiteScaffold(
    modifier: Modifier = Modifier,
    scaffoldSuiteItems: ScaffoldSuiteScope.() -> Unit,
    content: @Composable (PaddingValues?) -> Unit,
    scaffoldSuiteColors: ScaffoldSuiteColors = ScaffoldSuiteDefaults.colors(),
) {
    val windowAdaptiveSize = LocalWindowAdaptiveSize.current
    val scope by rememberStateOfItems(scaffoldSuiteItems)

    // TODO : Add expndable fabs like from https://github.com/suzdaleva/ComposeExpandableFAB?tab=readme-ov-file
    val anySnackBarShown by remember {
        /*derivedStateOf {
            scope.snackbarHostStates.any { it.state.currentSnackbarData == null }
        }*/
        // TODO : Make this work in the future
        mutableStateOf(false)
    }

    when {
        windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Compact -> Scaffold(
            modifier = modifier,
            snackbarHost = { StackedSnackbarHost(hostState = LocalSnackbarState.current,) },
            floatingActionButton = {
                scope.AnimatedFloatingActionButton(
                    anySnackBarShown = anySnackBarShown,
                    content ={
                        FloatingActionButton(
                            modifier = it.modifier,
                            onClick = it.onClick,
                            content = { it.icon(Modifier) }
                        )
                    }
                )
            },
            content = content,
            bottomBar = {
                NavigationBar(
                    modifier = modifier,
                    containerColor = scaffoldSuiteColors.navigationBarContainerColor,
                    contentColor = scaffoldSuiteColors.navigationBarContentColor,
                    content = {
                        scope.navigationItems.forEach {
                            NavigationBarItem(
                                modifier = it.modifier,
                                selected = it.selected,
                                onClick = it.onClick,
                                icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                                enabled = it.enabled,
                                label = it.label,
                                alwaysShowLabel = false,
                                colors = it.colors?.navigationBarItemColors ?: NavigationBarItemDefaults.colors(),
                                interactionSource = it.interactionSource
                            )
                        }
                    }
                )
            }
        )
        windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Medium || windowAdaptiveSize.heightSizeClass == WindowHeightSizeClass.Compact -> Row(modifier) {
            NavigationRail(
                containerColor = scaffoldSuiteColors.navigationRailContainerColor,
                contentColor = scaffoldSuiteColors.navigationRailContentColor,
                content = {
                    scope.header?.let {
                        it(false)
                    }

                    scope.navigationItems.forEach {
                        NavigationRailItem(
                            modifier = it.modifier,
                            selected = it.selected,
                            onClick = it.onClick,
                            icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                            enabled = it.enabled,
                            label = it.label,
                            alwaysShowLabel = true,
                            colors = it.colors?.navigationRailItemColors ?: NavigationRailItemDefaults.colors(),
                            interactionSource = it.interactionSource
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    scope.footer?.let {
                        it(false)
                    }
                }
            )

            Box {
                content(null)

                scope.AnimatedFloatingActionButton(
                    anySnackBarShown = anySnackBarShown,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp, end = 24.dp),
                    content = {
                        scope.floatingActionButtonItems.forEach {
                            when (it.label) {
                                null -> FloatingActionButton(
                                    modifier = it.modifier,
                                    onClick = it.onClick,
                                    content = { it.icon(Modifier) }
                                )
                                else -> ExtendedFloatingActionButton(
                                    modifier = it.modifier,
                                    onClick = it.onClick,
                                    icon = { it.icon(Modifier) },
                                    text = it.label
                                )
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    content = {
                        StackedSnackbarHost(hostState = LocalSnackbarState.current,)
                    }
                )
            }
        }
        else -> Row(modifier) {

            val isDesktop = windowAdaptiveSize.widthSizeClass == WindowWidthSizeClass.Expanded
                    && windowAdaptiveSize.heightSizeClass == WindowHeightSizeClass.Expanded

            PermanentDrawerSheet(
                // TODO : on hover expand / collapse
                drawerContainerColor = scaffoldSuiteColors.navigationDrawerContainerColor,
                drawerContentColor = scaffoldSuiteColors.navigationDrawerContentColor,
                content = {
                    scope.header?.let {
                        CompositionLocalProvider(value = LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                            it(isDesktop)
                        }
                    }
                    
                    // TODO : Update this
                    scope.navigationItems.forEach {
                        NavigationDrawerItem(
                            modifier = it.modifier,
                            selected = it.selected,
                            onClick = it.onClick,
                            icon = it.icon,
                            badge = it.badge,
                            label = { it.label?.invoke() },
                            colors = it.colors?.navigationDrawerItemColors ?: NavigationDrawerItemDefaults.colors(),
                            interactionSource = it.interactionSource
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    scope.footer?.let {
                        CompositionLocalProvider(value = LocalTextStyle provides MaterialTheme.typography.headlineSmall) {
                            it(isDesktop)
                        }
                    }
                }
            )

            Box {
                content(null)

                val textStyle : TextStyle
                val snackbarHeight : Dp

                if(isDesktop) {
                    textStyle = MaterialTheme.typography.headlineLarge
                    snackbarHeight = 144.dp
                } else {
                    textStyle = MaterialTheme.typography.bodyLarge
                    snackbarHeight = 96.dp
                }

                scope.AnimatedFloatingActionButton(
                    anySnackBarShown = anySnackBarShown,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp, end = 24.dp),
                    content = {
                        when (it.label) {
                            null -> LargeFloatingActionButton(
                                modifier = it.modifier,
                                onClick = it.onClick,
                                content = { it.icon(Modifier) }
                            )
                            // Edited source code of ExtendedFloatingActionButton
                            else -> {
                                val fabPaddingDp : Dp
                                val contentPaddingDp : Dp
                                val iconSize : Dp

                                if(isDesktop) {
                                    iconSize = 40.dp
                                    contentPaddingDp = 52.dp
                                    fabPaddingDp = 32.dp
                                } else {
                                    iconSize = 32.dp
                                    contentPaddingDp = 32.dp
                                    fabPaddingDp = 16.dp
                                }

                                FloatingActionButton(
                                    onClick = it.onClick,
                                    modifier = it.modifier.padding(
                                        bottom = fabPaddingDp,
                                        end = fabPaddingDp
                                    ),
                                    content = {

                                        Row(
                                            modifier = Modifier
                                                .sizeIn(minWidth = 152.dp, minHeight = 96.dp)
                                                .padding(
                                                    start = contentPaddingDp,
                                                    end = contentPaddingDp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            content = {

                                                it.icon(Modifier.size(iconSize))


                                                Spacer(Modifier.width((contentPaddingDp.value * 0.75).dp))

                                                CompositionLocalProvider(
                                                    value = LocalTextStyle provides textStyle,
                                                    content = {
                                                        it.label.invoke()
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    content = {
                        val typography = MaterialTheme.typography

                        MaterialTheme(
                            typography = typography.copy(
                                bodyMedium = textStyle,
                                labelLarge = textStyle.copy(fontSize = textStyle.fontSize * 0.75)
                            ),
                            content = {
                                StackedSnackbarHost(
                                    hostState = LocalSnackbarState.current,
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .heightIn(min = snackbarHeight)
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberStateOfItems(
    content: ScaffoldSuiteScope.() -> Unit
): State<ScaffoldSuiteScope> {
    val latestContent = rememberUpdatedState(content)
    return remember {
        derivedStateOf { ScaffoldSuiteScope().apply(latestContent.value) }
    }
}

@Composable
private fun NavigationItemIcon(
    icon: @Composable () -> Unit,
    badge: (@Composable () -> Unit)? = null,
) = if(badge == null) icon()  else BadgedBox(badge = { badge() }) { icon() }

@Composable
private fun ScaffoldSuiteScope.AnimatedFloatingActionButton(
    anySnackBarShown : Boolean,
    modifier: Modifier = Modifier,
    content : @Composable (ScaffoldSuiteScope.FloatingActionBarItem) -> Unit
) = AnimatedVisibility(
    modifier = modifier,
    visible = anySnackBarShown,
    exit = fadeOut() + shrinkHorizontally(
        tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    ),
    enter = fadeIn() + expandVertically(
        tween(
            durationMillis = 500,
            easing = LinearEasing
        )
    ),
    content = {
        Column {
            this@AnimatedFloatingActionButton.floatingActionButtonItems.forEach { 
                content(it)
            }
        }
    }
)