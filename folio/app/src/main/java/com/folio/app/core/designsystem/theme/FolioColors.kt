package com.folio.app.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A small, intentional colour contract. We don't lean on Material's full role
 * system because Folio's surface language is simpler: paper, raised paper, ink,
 * muted ink, a hairline divider, and a single accent.
 */
@Immutable
data class FolioColors(
    val paper: Color,
    val paperRaised: Color,
    val ink: Color,
    val inkMuted: Color,
    val hairline: Color,
    val accent: Color,
    val isDark: Boolean,
)

val LightFolioColors = FolioColors(
    paper = PaperLight,
    paperRaised = PaperRaisedLight,
    ink = InkLight,
    inkMuted = InkMutedLight,
    hairline = HairlineLight,
    accent = AccentLight,
    isDark = false,
)

val DarkFolioColors = FolioColors(
    paper = PaperDark,
    paperRaised = PaperRaisedDark,
    ink = InkDark,
    inkMuted = InkMutedDark,
    hairline = HairlineDark,
    accent = AccentDark,
    isDark = true,
)

val LocalFolioColors = staticCompositionLocalOf { LightFolioColors }
