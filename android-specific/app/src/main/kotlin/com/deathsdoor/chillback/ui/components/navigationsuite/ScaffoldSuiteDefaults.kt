package com.deathsdoor.chillback.ui.components.navigationsuite

import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ScaffoldSuiteDefaults {
    @Composable
    fun colors(
        navigationBarContainerColor: Color = NavigationBarDefaults.containerColor,
        navigationBarContentColor: Color = contentColorFor(navigationBarContainerColor),
        navigationRailContainerColor: Color = NavigationRailDefaults.ContainerColor,
        navigationRailContentColor: Color = contentColorFor(navigationRailContainerColor),
        navigationDrawerContainerColor: Color = DrawerDefaults.containerColor,
        navigationDrawerContentColor: Color = contentColorFor(navigationDrawerContainerColor),
    ): ScaffoldSuiteColors = ScaffoldSuiteColors(
        navigationBarContainerColor = navigationBarContainerColor,
        navigationBarContentColor = navigationBarContentColor,
        navigationRailContainerColor = navigationRailContainerColor,
        navigationRailContentColor = navigationRailContentColor,
        navigationDrawerContainerColor = navigationDrawerContainerColor,
        navigationDrawerContentColor = navigationDrawerContentColor
    )
}