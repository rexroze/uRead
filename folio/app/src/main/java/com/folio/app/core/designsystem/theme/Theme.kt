package com.folio.app.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Folio's theme. We keep our own [FolioColors] contract for the paper/ink
 * language, but still hand Material3 a matching [androidx.compose.material3.ColorScheme]
 * so stock components (sheets, ripples) feel native.
 *
 * Access Folio tokens anywhere via [FolioTheme].
 */
@Composable
fun FolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val folioColors = if (darkTheme) DarkFolioColors else LightFolioColors

    val materialScheme = if (darkTheme) {
        darkColorScheme(
            primary = folioColors.accent,
            background = folioColors.paper,
            surface = folioColors.paperRaised,
            onPrimary = folioColors.paper,
            onBackground = folioColors.ink,
            onSurface = folioColors.ink,
            outline = folioColors.hairline,
        )
    } else {
        lightColorScheme(
            primary = folioColors.accent,
            background = folioColors.paper,
            surface = folioColors.paperRaised,
            onPrimary = folioColors.paper,
            onBackground = folioColors.ink,
            onSurface = folioColors.ink,
            outline = folioColors.hairline,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalFolioColors provides folioColors,
        LocalSpacing provides Spacing,
    ) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = FolioTypography,
            shapes = FolioShapes,
            content = content,
        )
    }
}

/** Convenience accessor for Folio design tokens, mirroring `MaterialTheme`. */
object FolioTheme {
    val colors: FolioColors
        @Composable get() = LocalFolioColors.current

    val spacing: Spacing
        @Composable get() = LocalSpacing.current
}
