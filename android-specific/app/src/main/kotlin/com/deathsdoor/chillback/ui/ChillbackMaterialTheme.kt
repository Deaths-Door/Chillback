package com.deathsdoor.chillback.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.deathsdoor.chillback.ui.providers.LocalWindowAdaptiveSize

@Composable
fun ChillbackMaterialTheme(content : @Composable () -> Unit) {
    val typography = MaterialTheme.typography
    val windowSizeClass = LocalWindowAdaptiveSize.current
    MaterialTheme(
        colorScheme = DarkColors,
        typography = typography.copy(
            displayLarge = typography.displayLarge.scaleBasedOnRequirement(windowSizeClass),
            displayMedium = typography.displayMedium.scaleBasedOnRequirement(windowSizeClass),
            displaySmall = typography.displaySmall.scaleBasedOnRequirement(windowSizeClass),
            headlineLarge = typography.headlineLarge.scaleBasedOnRequirement(windowSizeClass),
            headlineMedium = typography.headlineMedium.scaleBasedOnRequirement(windowSizeClass),
            headlineSmall = typography.headlineSmall.scaleBasedOnRequirement(windowSizeClass),
            titleLarge = typography.titleLarge.scaleBasedOnRequirement(windowSizeClass),
            titleMedium = typography.titleMedium.scaleBasedOnRequirement(windowSizeClass),
            titleSmall = typography.titleSmall.scaleBasedOnRequirement(windowSizeClass),
            bodyLarge = typography.bodyLarge.scaleBasedOnRequirement(windowSizeClass),
            bodyMedium = typography.bodyMedium.scaleBasedOnRequirement(windowSizeClass),
            bodySmall = typography.bodySmall.scaleBasedOnRequirement(windowSizeClass),
            labelLarge = typography.labelLarge.scaleBasedOnRequirement(windowSizeClass),
            labelMedium = typography.labelMedium.scaleBasedOnRequirement(windowSizeClass),
            labelSmall = typography.labelSmall.scaleBasedOnRequirement(windowSizeClass),
        ),
        content = content
    )
}

private fun TextStyle.scaleBasedOnRequirement(windowSizeClass : WindowSizeClass): TextStyle {
    return when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> this
        WindowWidthSizeClass.Expanded -> this.copy(fontSize = fontSize * 2.5, lineHeight = lineHeight * 2.5)
        WindowWidthSizeClass.Medium -> this.copy(fontSize = fontSize * 1.5, lineHeight = lineHeight * 1.5)
        else -> this
    }
}

// TODO : REMOVE THIS LATER ON
val md_theme_dark_primary = Color(0xFFD0BCFF)
val md_theme_dark_onPrimary = Color(0xFF381E72)
val md_theme_dark_primaryContainer = Color(0xFF4F378B)
val md_theme_dark_onPrimaryContainer = Color(0xFFEADDFF)
val md_theme_dark_secondary = Color(0xFFCCC2DC)
val md_theme_dark_onSecondary = Color(0xFF332D41)
val md_theme_dark_secondaryContainer = Color(0xFF4A4458)
val md_theme_dark_onSecondaryContainer = Color(0xFFE8DEF8)
val md_theme_dark_tertiary = Color(0xFFEFB8C8)
val md_theme_dark_onTertiary = Color(0xFF492532)
val md_theme_dark_tertiaryContainer = Color(0xFF633B48)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD8E4)
val md_theme_dark_error = Color(0xFFF2B8B5)
val md_theme_dark_onError = Color(0xFF601410)
val md_theme_dark_errorContainer = Color(0xFF8C1D18)
val md_theme_dark_onErrorContainer = Color(0xFFF9DEDC)
val md_theme_dark_outline = Color(0xFF938F99)
val md_theme_dark_background = Color(0xFF1C1B1F)
val md_theme_dark_onBackground = Color(0xFFE6E1E5)
val md_theme_dark_surface = Color(0xFF1C1B1F)
val md_theme_dark_onSurface = Color(0xFFE6E1E5)
val md_theme_dark_surfaceVariant = Color(0xFF49454F)
val md_theme_dark_onSurfaceVariant = Color(0xFFCAC4D0)
val md_theme_dark_inverseSurface = Color(0xFFE6E1E5)
val md_theme_dark_inverseOnSurface = Color(0xFF313033)
val md_theme_dark_inversePrimary = Color(0xFF6750A4)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFD0BCFF)
val md_theme_dark_outlineVariant = Color(0xFF49454F)
val md_theme_dark_scrim = Color(0xFF000000)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    outline = md_theme_dark_outline,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

