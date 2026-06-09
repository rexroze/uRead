package com.folio.app.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.folio.app.core.designsystem.theme.CoverShape
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.model.Media
import kotlin.math.absoluteValue

/**
 * A cover. When [Media.coverUrl] is present we load it; otherwise we render a
 * calm typographic placeholder tinted by a deterministic colour derived from the
 * title. This keeps an empty/offline library looking intentional, not broken.
 *
 * Aspect ratio adapts to format: tall for books/manga, square for audio.
 */
@Composable
fun MediaCover(
    media: Media,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val colors = FolioTheme.colors
    val ratio = if (media.type.isAudio) 1f else 0.68f

    val base = modifier
        .aspectRatio(ratio)
        .clip(CoverShape)
        .border(1.dp, colors.hairline, CoverShape)
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)

    if (media.coverUrl != null) {
        AsyncImage(
            model = media.coverUrl,
            contentDescription = media.title,
            contentScale = ContentScale.Crop,
            modifier = base,
        )
    } else {
        PlaceholderCover(media = media, tint = placeholderTint(media.accentSeed, colors.isDark), modifier = base)
    }
}

@Composable
private fun PlaceholderCover(
    media: Media,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val colors = FolioTheme.colors
    Box(
        modifier = modifier.background(tint),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = media.type.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.inkMuted,
            )
            Text(
                text = media.title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.ink,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
    }
}

/** Muted, paper-friendly tint chosen deterministically from a seed. */
private fun placeholderTint(seed: Long, isDark: Boolean): Color {
    val hues = floatArrayOf(28f, 200f, 150f, 340f, 50f, 270f)
    val hue = hues[(seed.absoluteValue % hues.size).toInt()]
    return if (isDark) {
        Color.hsl(hue, 0.18f, 0.16f)
    } else {
        Color.hsl(hue, 0.22f, 0.90f)
    }
}
