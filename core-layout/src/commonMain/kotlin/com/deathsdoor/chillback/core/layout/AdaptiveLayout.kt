package com.deathsdoor.chillback.core.layout

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

/**
 * A composable that adapts its content based on the device's screen size and orientation.
 *
 * This composable allows you to define different layouts for mobile portrait, mobile landscape,
 * and desktop screen sizes. It achieves this by leveraging the `LocalWindowSize` composable
 * to access the current window size information.
 *
 * @param onMobilePortrait A composable function that represents the content to be displayed
 *  in mobile portrait mode.
 * @param onMobileLandscape A composable function that represents the content to be displayed
 *  in mobile landscape mode.
 * @param onDesktop A composable function that represents the content to be displayed
 *  on desktop screens.
 *  */
@Composable
fun AdaptiveLayout(
    onMobilePortrait : @Composable () -> Unit,
    onMobileLandscape : @Composable () -> Unit,
    onDesktop : @Composable () -> Unit
) {
    val windowSize = LocalWindowSize.current

    when(windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> onMobilePortrait()
//        WindowWidthSizeClass.Medium -> TABLET VARIANT
        else -> when(windowSize.heightSizeClass) {
            WindowHeightSizeClass.Compact -> onMobileLandscape()
            else -> onDesktop()
        }
    }
}

@Composable
fun AdaptiveLayout(
    onMobile : @Composable () -> Unit,
    onDesktop : @Composable () -> Unit
) {
    val windowSize = LocalWindowSize.current

    if(windowSize.widthSizeClass != WindowWidthSizeClass.Compact
        && windowSize.heightSizeClass != WindowHeightSizeClass.Compact) onDesktop()
    else onMobile()
}