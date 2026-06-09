package com.folio.app.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Soft, restrained corners. Covers (book/manga) stay nearly square so artwork
 * isn't clipped; surfaces get a gentle radius.
 */
val FolioShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

val CoverShape = RoundedCornerShape(8.dp)
