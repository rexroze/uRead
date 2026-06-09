package com.folio.app.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.component.MediaCover
import com.folio.app.core.designsystem.component.ProgressHairline
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Media
import com.folio.app.core.model.MediaType
import com.folio.app.data.mock.MockLibrary

/**
 * One unified library. Everything you own lives here; a single row of filter
 * chips narrows by format. No tabs, no per-type silos — that's the whole pitch
 * of an all-in-one reader.
 */
@Composable
fun LibraryScreen(
    onOpen: (String) -> Unit,
) {
    val colors = FolioTheme.colors
    var filter by remember { mutableStateOf<MediaType?>(null) }

    val items = remember(filter) {
        MockLibrary.library.filter { filter == null || it.type == filter }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 108.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = Spacing.gutter,
            end = Spacing.gutter,
            top = Spacing.xl,
            bottom = Spacing.xxl,
        ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        // Title spans the full row.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.ink,
                modifier = Modifier.padding(bottom = Spacing.md),
            )
        }

        // Filter chips span the full row.
        item(span = { GridItemSpan(maxLineSpan) }) {
            FilterRow(
                selected = filter,
                onSelect = { filter = it },
                modifier = Modifier.padding(bottom = Spacing.sm),
            )
        }

        items(items, key = { it.id }) { media ->
            LibraryCell(media = media, onClick = { onOpen(media.id) })
        }
    }
}

@Composable
private fun FilterRow(
    selected: MediaType?,
    onSelect: (MediaType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        FilterChip(label = "All", selected = selected == null, onClick = { onSelect(null) })
        // Only show chips for types actually present, keeps the row honest.
        MediaType.entries
            .filter { type -> MockLibrary.library.any { it.type == type } }
            .forEach { type ->
                FilterChip(
                    label = type.label,
                    selected = selected == type,
                    onClick = { onSelect(type) },
                )
            }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    val shape = RoundedCornerShape(50)
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = if (selected) colors.paper else colors.inkMuted,
        modifier = Modifier
            .clip(shape)
            .background(if (selected) colors.ink else colors.paper)
            .border(1.dp, if (selected) colors.ink else colors.hairline, shape)
            .clickable { onClick() }
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
    )
}

@Composable
private fun LibraryCell(
    media: Media,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        MediaCover(media = media, onClick = onClick)
        if (media.isStarted) {
            ProgressHairline(progress = media.progress)
        }
        Text(
            text = media.title,
            style = MaterialTheme.typography.titleSmall,
            color = colors.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = media.author,
            style = MaterialTheme.typography.labelSmall,
            color = colors.inkMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
