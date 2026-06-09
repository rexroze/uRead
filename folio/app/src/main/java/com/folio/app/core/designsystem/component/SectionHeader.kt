package com.folio.app.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.folio.app.core.designsystem.theme.FolioTheme

/**
 * Quiet section label used on the home feed. Optional trailing action (e.g.
 * "See all") rendered in the accent colour.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )
        if (action != null && onAction != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelMedium,
                color = colors.accent,
                modifier = Modifier.clickable { onAction() },
            )
        }
    }
}
