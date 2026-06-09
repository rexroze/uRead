package com.folio.app.feature.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing

/**
 * Settings screen. State is in-memory; wire to DataStore when persistence lands.
 */
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val colors = FolioTheme.colors

    // 0=System, 1=Light, 2=Dark
    var theme by remember { mutableIntStateOf(0) }
    // 0=Small, 1=Medium, 2=Large
    var fontSize by remember { mutableIntStateOf(1) }
    // 0=LTR, 1=RTL
    var readingDirection by remember { mutableIntStateOf(0) }
    // 0=Auto, 1=2-col, 2=3-col
    var gridColumns by remember { mutableIntStateOf(0) }
    // 0=Title, 1=Author, 2=Recently added, 3=Progress
    var sortBy by remember { mutableIntStateOf(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = colors.ink)
            }
            Spacer(Modifier.width(Spacing.xs))
            Text("Settings", style = MaterialTheme.typography.headlineMedium, color = colors.ink)
        }

        SettingsSection("Appearance") {
            SegmentedSetting(
                label = "Theme",
                options = listOf("System", "Light", "Dark"),
                selected = theme,
                onSelect = { theme = it },
            )
        }

        SettingsSection("Reader") {
            SegmentedSetting(
                label = "Font size",
                options = listOf("S", "M", "L"),
                selected = fontSize,
                onSelect = { fontSize = it },
            )
            HorizontalDivider(color = colors.hairline)
            SegmentedSetting(
                label = "Direction",
                options = listOf("L → R", "R → L"),
                selected = readingDirection,
                onSelect = { readingDirection = it },
            )
        }

        SettingsSection("Library") {
            SegmentedSetting(
                label = "Grid",
                options = listOf("Auto", "2", "3"),
                selected = gridColumns,
                onSelect = { gridColumns = it },
            )
            HorizontalDivider(color = colors.hairline)
            SegmentedSetting(
                label = "Sort by",
                options = listOf("Title", "Author", "Added", "Progress"),
                selected = sortBy,
                onSelect = { sortBy = it },
            )
        }

        SettingsSection("About") {
            TappableSetting(label = "Version", value = "0.1.0", onClick = null)
            HorizontalDivider(color = colors.hairline)
            TappableSetting(label = "Open source licences", onClick = {})
        }

        Spacer(Modifier.height(Spacing.xxl))
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    val colors = FolioTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.gutter)
            .padding(top = Spacing.lg),
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = colors.inkMuted,
            modifier = Modifier.padding(bottom = Spacing.sm),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, colors.hairline, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
        ) {
            content()
        }
    }
}

@Composable
private fun SegmentedSetting(
    label: String,
    options: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = colors.ink)

        // Inline segment control.
        Row(
            modifier = Modifier
                .border(1.dp, colors.hairline, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
        ) {
            options.forEachIndexed { idx, opt ->
                val isSelected = idx == selected
                Box(
                    modifier = Modifier
                        .clickable { onSelect(idx) }
                        .then(
                            if (isSelected)
                                Modifier.then(
                                    Modifier.border(0.dp, colors.ink)
                                )
                            else Modifier
                        )
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = opt,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) colors.accent else colors.inkMuted,
                    )
                }
            }
        }
    }
}

@Composable
private fun TappableSetting(
    label: String,
    value: String? = null,
    onClick: (() -> Unit)?,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = Spacing.md, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = colors.ink)
        if (value != null) {
            Text(value, style = MaterialTheme.typography.bodyMedium, color = colors.inkMuted)
        } else if (onClick != null) {
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = colors.inkMuted)
        }
    }
}
