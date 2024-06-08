package com.deathsdoor.chillback.core.layout.navigationsuite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.BadgedBox
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.AdaptiveLayout
import com.deathsdoor.chillback.core.layout.LocalSnackbarState
import com.deathsdoor.chillback.core.layout.snackbar.StackedSnackbarHost

@Composable
fun ScaffoldSuite(
    modifier: Modifier = Modifier,
    scaffoldSuiteItems: ScaffoldSuiteScope.() -> Unit,
    content: @Composable (PaddingValues?) -> Unit,
    scaffoldSuiteColors: ScaffoldSuiteColors = ScaffoldSuiteDefaults.colors(),
) {
    val scope by rememberStateOfItems(scaffoldSuiteItems)

    AdaptiveLayout(
        onMobilePortrait = {
            Scaffold(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surface,
                snackbarHost = { StackedSnackbarHost(hostState = LocalSnackbarState.current) },
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
        },
        onMobileLandscape = {
            Surface(modifier = modifier) {
                Row {
                    NavigationRail(
                        containerColor = scaffoldSuiteColors.navigationRailContainerColor,
                        contentColor = scaffoldSuiteColors.navigationRailContentColor,
                        content = {
                            scope.header?.let {
                                it()
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
                                it()
                            }
                        }
                    )

                    Box {
                        content(null)

                        Box(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            content = {
                                StackedSnackbarHost(hostState = LocalSnackbarState.current,)
                            }
                        )
                    }
                }
            }
        },
        onDesktop = {
            Surface(modifier = modifier) {
                Row {
                    PermanentDrawerSheet(
                        drawerContainerColor = scaffoldSuiteColors.navigationDrawerContainerColor,
                        drawerContentColor = scaffoldSuiteColors.navigationDrawerContentColor,
                        content = {
                            scope.header?.let {
                                CompositionLocalProvider(value = LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                                    it()
                                }
                            }

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
                                    it()
                                }
                            }
                        }
                    )
                }

                Box {
                    content(null)

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        StackedSnackbarHost(
                            hostState = LocalSnackbarState.current,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .heightIn(min = 144.dp)
                        )
                    }
                }
            }
        }
    )
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
