package com.folio.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.component.MediaCover
import com.folio.app.core.designsystem.component.ProgressHairline
import com.folio.app.core.designsystem.component.SectionHeader
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Media
import com.folio.app.data.mock.MockLibrary

/**
 * Home is intentionally sparse: a quiet wordmark, what you're in the middle of,
 * and a short "next up" list. No banners, no recommendations carousel screaming
 * for attention.
 */
@Composable
fun HomeScreen(
    onOpen: (String) -> Unit,
    onContinue: (String) -> Unit,
) {
    val colors = FolioTheme.colors
    val continueReading = MockLibrary.continueReading
    val upNext = MockLibrary.library.filter { it.progress == 0f }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = Spacing.gutter,
            end = Spacing.gutter,
            top = Spacing.xl,
            bottom = Spacing.xxl,
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
    ) {
        item {
            Text(
                text = "Folio",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.ink,
            )
        }

        if (continueReading.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    SectionHeader(title = "Continue")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        items(continueReading, key = { it.id }) { media ->
                            ContinueCard(
                                media = media,
                                onClick = { onContinue(media.id) },
                            )
                        }
                    }
                }
            }
        }

        if (upNext.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    SectionHeader(title = "Up next")
                }
            }
            items(upNext, key = { it.id }) { media ->
                UpNextRow(media = media, onClick = { onOpen(media.id) })
            }
        }
    }
}

@Composable
private fun ContinueCard(
    media: Media,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    Column(
        modifier = Modifier.width(132.dp),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        MediaCover(media = media, onClick = onClick)
        ProgressHairline(progress = media.progress)
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

@Composable
private fun UpNextRow(
    media: Media,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        MediaCover(
            media = media,
            modifier = Modifier.width(56.dp),
            onClick = onClick,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = media.title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.ink,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${media.author} · ${media.type.label}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.height(1.dp))
    }
}
