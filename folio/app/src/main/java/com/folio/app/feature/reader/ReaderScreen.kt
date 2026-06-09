package com.folio.app.feature.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Replay10
import androidx.compose.material.icons.outlined.Forward30
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.component.MediaCover
import com.folio.app.core.designsystem.component.ProgressHairline
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Media
import com.folio.app.data.mock.MockLibrary

/**
 * The reading room. Tap anywhere to toggle the chrome (top/bottom bars); by
 * default it's hidden so the content is full-bleed. Branches by format:
 * audiobooks get a player, everything else gets the text/page surface.
 *
 * This is the immersive shell — a real Readium navigator (ebooks) / image pager
 * (manga) / ExoPlayer (audio) slots into the marked body region.
 */
@Composable
fun ReaderScreen(
    mediaId: String,
    onBack: () -> Unit,
) {
    val colors = FolioTheme.colors
    val media = MockLibrary.byId(mediaId)
    var chromeVisible by remember { mutableStateOf(false) }
    val noIndication = remember { MutableInteractionSource() }

    if (media == null) {
        Box(Modifier.fillMaxSize().background(colors.paper), contentAlignment = Alignment.Center) {
            Text("Not found", color = colors.inkMuted)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .clickable(
                interactionSource = noIndication,
                indication = null,
            ) { chromeVisible = !chromeVisible },
    ) {
        if (media.type.isAudio) {
            AudioBody(media = media)
        } else {
            TextBody(media = media)
        }

        // Top chrome.
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.paper)
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onBack() }
                        .padding(Spacing.sm),
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.ink,
                    )
                }
                Spacer(Modifier.size(Spacing.sm))
                Text(
                    text = media.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.ink,
                )
            }
        }

        // Bottom chrome — progress + position.
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.paper)
                    .navigationBarsPadding()
                    .padding(horizontal = Spacing.gutter, vertical = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                ProgressHairline(progress = media.progress)
                Text(
                    text = positionLabel(media),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.inkMuted,
                )
            }
        }
    }
}

@Composable
private fun TextBody(media: Media) {
    val colors = FolioTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = Spacing.xl, vertical = Spacing.xxl),
    ) {
        Text(
            text = "Chapter ${(media.progress * 12).toInt() + 1}",
            style = MaterialTheme.typography.labelMedium,
            color = colors.inkMuted,
        )
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = SAMPLE_PROSE,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.ink,
        )
        Spacer(Modifier.height(Spacing.xxl))
    }
}

@Composable
private fun AudioBody(media: Media) {
    val colors = FolioTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        MediaCover(media = media, modifier = Modifier.fillMaxWidth(0.62f))
        Spacer(Modifier.height(Spacing.xl))
        Text(
            text = media.title,
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.xs))
        Text(
            text = media.author,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.inkMuted,
        )
        Spacer(Modifier.height(Spacing.xxl))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl),
        ) {
            TransportButton(Icons.Outlined.Replay10, "Back 10s", 28.dp)
            TransportButton(Icons.Outlined.PlayArrow, "Play", 40.dp)
            TransportButton(Icons.Outlined.Forward30, "Forward 30s", 28.dp)
        }
    }
}

@Composable
private fun TransportButton(icon: ImageVector, description: String, size: androidx.compose.ui.unit.Dp) {
    val colors = FolioTheme.colors
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = colors.ink,
        modifier = Modifier
            .clickable { /* transport — stub */ }
            .padding(Spacing.sm)
            .size(size),
    )
}

private fun positionLabel(media: Media): String = when {
    media.type.isAudio -> {
        val curMin = media.currentUnit / 60
        val totMin = media.totalUnits / 60
        "$curMin of $totMin min"
    }
    else -> "Page ${media.currentUnit} of ${media.totalUnits}"
}

private const val SAMPLE_PROSE =
    "The light at the end of the day fell in long bars across the floor, and the " +
        "room held its quiet the way old rooms do. She turned the page without " +
        "thinking, the paper soft from a hundred readings before hers.\n\n" +
        "Folio is a reading place first and an app second. Nothing here asks to be " +
        "tapped; the words simply sit and wait. When you want the controls, a touch " +
        "brings them; when you don't, they are gone, and only the chapter remains.\n\n" +
        "This passage is placeholder text. Wire a Readium navigator into the body " +
        "region and these paragraphs become real pages, reflowed to the reader's " +
        "chosen font, size, and theme."
