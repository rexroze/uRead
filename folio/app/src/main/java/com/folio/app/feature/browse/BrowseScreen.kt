package com.folio.app.feature.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Source
import com.folio.app.data.mock.MockLibrary

/**
 * Browse lists online sources (the "extensions" surface). Installed sources sit
 * up top; the rest are one tap from being added. Tapping a source would open its
 * catalog — wired as a no-op stub for now.
 */
@Composable
fun BrowseScreen() {
    val colors = FolioTheme.colors
    val sources = MockLibrary.sources.sortedByDescending { it.isInstalled }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = Spacing.gutter,
            end = Spacing.gutter,
            top = Spacing.xl,
            bottom = Spacing.xxl,
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        item {
            Text(
                text = "Browse",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.ink,
                modifier = Modifier.padding(bottom = Spacing.sm),
            )
        }
        items(sources, key = { it.id }) { source ->
            SourceRow(source = source)
        }
    }
}

@Composable
private fun SourceRow(source: Source) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        // Monogram badge.
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.paperRaised)
                .border(1.dp, colors.hairline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = source.name.take(1),
                style = MaterialTheme.typography.titleMedium,
                color = colors.accent,
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = source.name,
                style = MaterialTheme.typography.titleMedium,
                color = colors.ink,
            )
            Text(
                text = "${source.supports.label} · ${source.language.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
            )
        }

        InstallAffordance(installed = source.isInstalled)
    }
}

@Composable
private fun InstallAffordance(installed: Boolean) {
    val colors = FolioTheme.colors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, colors.hairline, RoundedCornerShape(50))
            .clickable { /* install / open catalog — stub */ }
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Icon(
                imageVector = if (installed) Icons.Outlined.Check else Icons.Outlined.Add,
                contentDescription = if (installed) "Installed" else "Add source",
                tint = if (installed) colors.accent else colors.inkMuted,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = if (installed) "Added" else "Add",
                style = MaterialTheme.typography.labelMedium,
                color = if (installed) colors.accent else colors.inkMuted,
            )
        }
    }
}
