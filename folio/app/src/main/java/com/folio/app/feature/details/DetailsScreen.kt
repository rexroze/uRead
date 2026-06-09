package com.folio.app.feature.details

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.component.MediaCover
import com.folio.app.core.designsystem.component.ProgressHairline
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Media
import com.folio.app.core.model.MediaOrigin
import com.folio.app.data.mock.MockLibrary

/**
 * Detail view for a single entry. Cover, the essentials, one primary action
 * (Read or Listen depending on format), then a placeholder synopsis. Kept calm —
 * metadata is secondary to the act of opening the thing.
 */
@Composable
fun DetailsScreen(
    mediaId: String,
    onBack: () -> Unit,
    onRead: () -> Unit,
) {
    val colors = FolioTheme.colors
    val media = MockLibrary.byId(mediaId)

    if (media == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Not found", color = colors.inkMuted)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Top bar.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(icon = Icons.AutoMirrored.Outlined.ArrowBack, description = "Back", onClick = onBack)
        }

        Column(
            modifier = Modifier.padding(horizontal = Spacing.gutter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MediaCover(media = media, modifier = Modifier.width(168.dp))

            Spacer(Modifier.height(Spacing.lg))

            Text(
                text = media.title,
                style = MaterialTheme.typography.headlineMedium,
                color = colors.ink,
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = media.author,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = sourceLine(media),
                style = MaterialTheme.typography.labelMedium,
                color = colors.inkMuted,
            )

            Spacer(Modifier.height(Spacing.lg))

            if (media.isStarted) {
                ProgressHairline(progress = media.progress, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(Spacing.lg))
            }

            PrimaryAction(
                label = primaryLabel(media),
                icon = if (media.type.isAudio) Icons.Outlined.Headphones else Icons.Outlined.MenuBook,
                onClick = onRead,
            )

            Spacer(Modifier.height(Spacing.xl))

            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = colors.ink,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.sm))
            Text(
                text = "Synopsis and chapter list load here once a real source or " +
                    "local-file parser is wired in. For now this is placeholder copy " +
                    "to show the reading-room layout and typography.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.inkMuted,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.xxl))
        }
    }
}

private fun primaryLabel(media: Media): String = when {
    media.isFinished -> if (media.type.isAudio) "Listen again" else "Read again"
    media.isStarted -> if (media.type.isAudio) "Resume" else "Continue"
    else -> if (media.type.isAudio) "Listen" else "Read"
}

private fun sourceLine(media: Media): String = when (val o = media.origin) {
    is MediaOrigin.Local -> "${media.type.label} · On device"
    is MediaOrigin.Online -> "${media.type.label} · ${o.sourceName}"
}

@Composable
private fun PrimaryAction(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .clickable { onClick() }
            .background(colors.ink, shape),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = colors.paper, modifier = Modifier.width(20.dp))
        Spacer(Modifier.width(Spacing.sm))
        Text(label, style = MaterialTheme.typography.titleSmall, color = colors.paper)
    }
}

@Composable
private fun IconButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(Spacing.sm),
    ) {
        Icon(icon, contentDescription = description, tint = colors.ink, modifier = Modifier.width(24.dp))
    }
}
