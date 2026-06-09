package com.folio.app.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * A single spacing scale keeps rhythm consistent. Reach for these instead of
 * sprinkling magic dp values around screens.
 */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp

    /** Standard horizontal page gutter. */
    val gutter = 20.dp
}

val LocalSpacing = staticCompositionLocalOf { Spacing }
