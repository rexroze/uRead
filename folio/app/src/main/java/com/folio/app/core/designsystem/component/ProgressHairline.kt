package com.folio.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme

/**
 * A 2dp progress line — the only progress affordance in the app. No percentages
 * shouted at the reader; just a quiet mark of how far they are.
 */
@Composable
fun ProgressHairline(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val colors = FolioTheme.colors
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .clip(RoundedCornerShape(1.dp))
            .background(colors.hairline),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(colors.accent),
        )
    }
}
