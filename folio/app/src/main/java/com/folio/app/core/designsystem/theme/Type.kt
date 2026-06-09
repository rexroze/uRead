package com.folio.app.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography is the main expressive tool in a paper-like app. We use a serif for
 * titles (it reads as "book") and the system sans for UI labels. Swap
 * [SerifFamily] for a bundled font (e.g. Lora / Source Serif) in res/font when
 * you want the look fully locked-in across devices.
 */
val SerifFamily = FontFamily.Serif
val SansFamily = FontFamily.SansSerif

val FolioTypography = Typography(
    // Screen titles — set in serif, generous.
    headlineLarge = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    ),
    // Item titles (a book/manga name on a card).
    titleMedium = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp,
    ),
    // Reading / body copy.
    bodyLarge = TextStyle(
        fontFamily = SerifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 27.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Secondary labels / metadata.
    labelMedium = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.6.sp,
    ),
)
