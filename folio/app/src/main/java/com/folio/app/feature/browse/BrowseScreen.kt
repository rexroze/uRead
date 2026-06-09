package com.folio.app.feature.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.folio.app.core.designsystem.component.SectionHeader
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.ExtensionEntry
import com.folio.app.core.model.ExtensionRepo
import com.folio.app.core.model.Source
import com.folio.app.data.mock.MockLibrary

private const val KEIYOUSHI_URL = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.min.json"

/**
 * Browse surfaces two things: built-in sources (local mock) and online extension
 * repos. Tap "Add repo" to paste a URL — the keiyoushi repo is pre-filled as an
 * example. Sources fetched from repos appear below the built-ins.
 */
@Composable
fun BrowseScreen(
    onMenuOpen: () -> Unit,
    viewModel: BrowseViewModel = hiltViewModel(),
) {
    val colors = FolioTheme.colors
    val state by viewModel.state.collectAsState()
    var showAddRepo by remember { mutableStateOf(false) }
    var repoUrlInput by remember { mutableStateOf(KEIYOUSHI_URL) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Browse", style = MaterialTheme.typography.headlineLarge, color = colors.ink)
                IconButton(onClick = onMenuOpen) {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = "Menu",
                        tint = colors.inkMuted,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }

        // ---- Built-in / installed sources ----
        item {
            SectionHeader(title = "Installed")
        }
        items(MockLibrary.sources, key = { it.id }) { source ->
            BuiltInSourceRow(source = source)
        }

        // ---- Extension repos ----
        item { Spacer(Modifier.height(Spacing.sm)) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SectionHeader(title = "Extension repos")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, colors.hairline, RoundedCornerShape(50))
                        .clickable { showAddRepo = !showAddRepo }
                        .padding(horizontal = Spacing.md, vertical = Spacing.xs),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        Icon(
                            if (showAddRepo) Icons.Outlined.Close else Icons.Outlined.Add,
                            contentDescription = if (showAddRepo) "Cancel" else "Add repo",
                            tint = colors.ink,
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = if (showAddRepo) "Cancel" else "Add",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.ink,
                        )
                    }
                }
            }
        }

        if (showAddRepo) {
            item {
                AddRepoRow(
                    value = repoUrlInput,
                    onValueChange = { repoUrlInput = it },
                    onAdd = {
                        viewModel.addRepo(repoUrlInput)
                        showAddRepo = false
                    },
                )
            }
        }

        // Added repos list.
        items(state.repos, key = { "repo_${it.url}" }) { repo ->
            RepoRow(
                repo = repo,
                isLoading = repo.url in state.loading,
                error = state.errors[repo.url],
                extensionCount = state.extensions[repo.url]?.size ?: 0,
                onRemove = { viewModel.removeRepo(repo.url) },
            )
        }

        // Extensions from repos.
        val allExtensions = state.extensions.values.flatten()
        if (allExtensions.isNotEmpty()) {
            item { Spacer(Modifier.height(Spacing.sm)) }
            item { SectionHeader(title = "Extensions") }
            items(allExtensions, key = { "ext_${it.pkg}" }) { ext ->
                ExtensionRow(ext = ext)
            }
        }
    }
}

@Composable
private fun BuiltInSourceRow(source: Source) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.paperRaised)
                .border(1.dp, colors.hairline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(source.name.take(1), style = MaterialTheme.typography.titleMedium, color = colors.accent)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(source.name, style = MaterialTheme.typography.titleMedium, color = colors.ink)
            Text(
                text = "${source.supports.label} · ${source.language.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
            )
        }
        Icon(
            Icons.Outlined.Check,
            contentDescription = "Installed",
            tint = colors.accent,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun AddRepoRow(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
) {
    val colors = FolioTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.hairline, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text("Paste a repo index URL", style = MaterialTheme.typography.labelMedium, color = colors.inkMuted)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, colors.hairline, RoundedCornerShape(8.dp))
                .padding(Spacing.sm),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.ink),
                cursorBrush = SolidColor(colors.accent),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (value.isNotBlank()) colors.ink else colors.hairline)
                .clickable(enabled = value.isNotBlank()) { onAdd() }
                .padding(vertical = Spacing.sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Add repo",
                style = MaterialTheme.typography.labelMedium,
                color = if (value.isNotBlank()) colors.paper else colors.inkMuted,
            )
        }
    }
}

@Composable
private fun RepoRow(
    repo: ExtensionRepo,
    isLoading: Boolean,
    error: String?,
    extensionCount: Int,
    onRemove: () -> Unit,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.paperRaised)
                .border(1.dp, colors.hairline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = colors.accent,
                )
            } else {
                Icon(
                    Icons.Outlined.HourglassEmpty,
                    contentDescription = null,
                    tint = colors.inkMuted,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = repo.url.removePrefix("https://").take(44),
                style = MaterialTheme.typography.labelMedium,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = when {
                    isLoading -> "Loading…"
                    error != null -> "Error: $error"
                    extensionCount > 0 -> "$extensionCount extensions"
                    else -> "No extensions found"
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (error != null) colors.accent else colors.inkMuted,
            )
        }
        Icon(
            Icons.Outlined.Delete,
            contentDescription = "Remove repo",
            tint = colors.inkMuted,
            modifier = Modifier
                .size(18.dp)
                .clickable { onRemove() },
        )
    }
}

@Composable
private fun ExtensionRow(ext: ExtensionEntry) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.paperRaised)
                .border(1.dp, colors.hairline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(ext.name.take(1), style = MaterialTheme.typography.titleMedium, color = colors.accent)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                ext.name,
                style = MaterialTheme.typography.titleMedium,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${ext.lang.uppercase()} · v${ext.version}" +
                    if (ext.nsfw == 1) " · 18+" else "",
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .border(1.dp, colors.hairline, RoundedCornerShape(50))
                .clickable { /* install — stub */ }
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        ) {
            Text("Install", style = MaterialTheme.typography.labelMedium, color = colors.inkMuted)
        }
    }
}
