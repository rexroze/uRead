package com.folio.app.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.MediaType
import com.folio.app.data.mock.MockAnnotations
import com.folio.app.data.mock.MockLibrary

@Composable
fun StatisticsScreen(onBack: () -> Unit) {
    val colors = FolioTheme.colors
    val library = MockLibrary.library

    val stats = remember {
        Stats(
            total = library.size,
            inProgress = library.count { it.isStarted },
            completed = library.count { it.isFinished },
            notStarted = library.count { !it.isStarted && !it.isFinished },
            pagesRead = library
                .filter { !it.type.isAudio }
                .sumOf { (it.progress * it.totalUnits).toInt() },
            hoursListened = library
                .filter { it.type.isAudio }
                .sumOf { (it.progress * it.totalUnits / 3600.0) },
            annotations = MockAnnotations.annotations.size,
            byType = MediaType.entries.associateWith { type ->
                library.count { it.type == type }
            },
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Spacing.xxl),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.ink,
                    )
                }
                Spacer(Modifier.width(Spacing.xs))
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.ink,
                )
            }
        }

        item {
            // Primary stat row: total, in progress, completed.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.gutter, vertical = Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                BigStatCard("Total", stats.total.toString(), modifier = Modifier.weight(1f))
                BigStatCard("Reading", stats.inProgress.toString(), modifier = Modifier.weight(1f))
                BigStatCard("Done", stats.completed.toString(), modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.gutter),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                BigStatCard(
                    label = "Pages read",
                    value = stats.pagesRead.formatLarge(),
                    modifier = Modifier.weight(1f),
                )
                BigStatCard(
                    label = "Hours listened",
                    value = "%.1f".format(stats.hoursListened),
                    modifier = Modifier.weight(1f),
                )
                BigStatCard(
                    label = "Annotations",
                    value = stats.annotations.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item { Spacer(Modifier.height(Spacing.xl)) }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.gutter),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                Text(
                    text = "By format",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.ink,
                )
                Spacer(Modifier.height(Spacing.md))
                MediaType.entries.filter { (stats.byType[it] ?: 0) > 0 }.forEach { type ->
                    val count = stats.byType[type] ?: 0
                    val fraction = if (stats.total > 0) count.toFloat() / stats.total else 0f
                    FormatRow(type.label, count, fraction)
                    HorizontalDivider(color = colors.hairline, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun BigStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    val colors = FolioTheme.colors
    Box(
        modifier = modifier
            .border(1.dp, colors.hairline, RoundedCornerShape(12.dp))
            .padding(Spacing.md),
    ) {
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = colors.ink,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
            )
        }
    }
}

@Composable
private fun FormatRow(label: String, count: Int, fraction: Float) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = colors.ink)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            // Thin bar visualising the fraction.
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.hairline),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction.coerceIn(0f, 1f))
                        .height(3.dp)
                        .background(colors.accent),
                )
            }
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
                modifier = Modifier.width(20.dp),
            )
        }
    }
}

private data class Stats(
    val total: Int,
    val inProgress: Int,
    val completed: Int,
    val notStarted: Int,
    val pagesRead: Int,
    val hoursListened: Double,
    val annotations: Int,
    val byType: Map<MediaType, Int>,
)

private fun Int.formatLarge(): String = when {
    this >= 1000 -> "%.1fk".format(this / 1000f)
    else -> toString()
}
