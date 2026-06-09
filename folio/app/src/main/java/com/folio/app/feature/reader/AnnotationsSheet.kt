package com.folio.app.feature.reader

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FormatColorText
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing
import com.folio.app.core.model.Annotation
import com.folio.app.core.model.AnnotationColor
import com.folio.app.core.model.AnnotationType
import com.folio.app.data.mock.MockAnnotations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnotationsSheet(
    mediaId: String,
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    val colors = FolioTheme.colors
    var showAddForm by remember { mutableStateOf(false) }
    var annotations by remember { mutableStateOf(MockAnnotations.forMedia(mediaId)) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.paper,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = Spacing.sm)
                    .size(width = 36.dp, height = 3.dp)
                    .clip(CircleShape)
                    .background(colors.hairline),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.gutter),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Notes & highlights", style = MaterialTheme.typography.headlineMedium, color = colors.ink)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable { showAddForm = !showAddForm }
                        .border(1.dp, colors.hairline, RoundedCornerShape(50))
                        .padding(horizontal = Spacing.md, vertical = Spacing.xs),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        Icon(
                            if (showAddForm) Icons.Outlined.Close else Icons.Outlined.Add,
                            contentDescription = if (showAddForm) "Cancel" else "Add",
                            tint = colors.ink,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = if (showAddForm) "Cancel" else "Add",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.ink,
                        )
                    }
                }
            }

            Spacer(Modifier.height(Spacing.lg))

            if (showAddForm) {
                AddAnnotationForm(
                    mediaId = mediaId,
                    onSave = { newAnnotation ->
                        MockAnnotations.add(newAnnotation)
                        annotations = MockAnnotations.forMedia(mediaId)
                        showAddForm = false
                    },
                    onDismiss = { showAddForm = false },
                )
                Spacer(Modifier.height(Spacing.lg))
            }

            if (annotations.isEmpty() && !showAddForm) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.xxl),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No annotations yet. Highlights, underlines and notes you add will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkMuted,
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = Spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                items(annotations, key = { it.id }) { annotation ->
                    AnnotationCard(
                        annotation = annotation,
                        onDelete = {
                            MockAnnotations.remove(annotation.id)
                            annotations = MockAnnotations.forMedia(mediaId)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AnnotationCard(
    annotation: Annotation,
    onDelete: () -> Unit,
) {
    val colors = FolioTheme.colors
    val accentColor = annotationAccentColor(annotation.color, colors.isDark)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.hairline, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Icon(
                    imageVector = annotationIcon(annotation.type),
                    contentDescription = annotation.type.name,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = annotation.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                )
                Text(
                    text = " · p. ${annotation.pageOrPosition}",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.inkMuted,
                )
            }
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = colors.inkMuted,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onDelete() },
            )
        }

        Text(
            text = "“${annotation.selectedText}”",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.ink,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )

        if (annotation.note != null) {
            Text(
                text = annotation.note,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
            )
        }
    }
}

@Composable
private fun AddAnnotationForm(
    mediaId: String,
    onSave: (Annotation) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = FolioTheme.colors
    var selectedType by remember { mutableStateOf(AnnotationType.HIGHLIGHT) }
    var selectedColor by remember { mutableStateOf(AnnotationColor.YELLOW) }
    var textInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.hairline, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        // Type selector.
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            AnnotationType.entries.forEach { type ->
                val selected = type == selectedType
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, if (selected) colors.ink else colors.hairline, RoundedCornerShape(50))
                        .background(if (selected) colors.ink else colors.paper)
                        .clickable { selectedType = type }
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                ) {
                    Text(
                        text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) colors.paper else colors.inkMuted,
                    )
                }
            }
        }

        // Colour dots.
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            AnnotationColor.entries.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(annotationAccentColor(color, colors.isDark))
                        .border(
                            width = if (color == selectedColor) 2.dp else 0.dp,
                            color = colors.ink,
                            shape = CircleShape,
                        )
                        .clickable { selectedColor = color },
                )
            }
        }

        // Selected text.
        SimpleTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = "Paste or type the selected text",
        )

        // Note (only for NOTE and optionally others).
        SimpleTextField(
            value = noteInput,
            onValueChange = { noteInput = it },
            placeholder = "Add a note (optional)",
        )

        // Save button.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (textInput.isNotBlank()) colors.ink else colors.hairline)
                .clickable(enabled = textInput.isNotBlank()) {
                    onSave(
                        Annotation(
                            mediaId = mediaId,
                            type = selectedType,
                            selectedText = textInput.trim(),
                            note = noteInput.trim().ifBlank { null },
                            color = selectedColor,
                        )
                    )
                }
                .padding(vertical = Spacing.md),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Save",
                style = MaterialTheme.typography.titleSmall,
                color = if (textInput.isNotBlank()) colors.paper else colors.inkMuted,
            )
        }
    }
}

@Composable
private fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    val colors = FolioTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.hairline, RoundedCornerShape(10.dp))
            .padding(Spacing.md),
    ) {
        if (value.isEmpty()) {
            Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = colors.inkMuted)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.ink),
            cursorBrush = SolidColor(colors.accent),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun annotationIcon(type: AnnotationType): ImageVector = when (type) {
    AnnotationType.HIGHLIGHT -> Icons.Outlined.FormatColorText
    AnnotationType.UNDERLINE -> Icons.Outlined.FormatUnderlined
    AnnotationType.NOTE -> Icons.Outlined.StickyNote2
}

private fun annotationAccentColor(color: AnnotationColor, isDark: Boolean): Color = when (color) {
    AnnotationColor.YELLOW -> if (isDark) Color(0xFFD4AC5A) else Color(0xFF9E7A10)
    AnnotationColor.GREEN -> if (isDark) Color(0xFF6AAB7C) else Color(0xFF2A7A45)
    AnnotationColor.BLUE -> if (isDark) Color(0xFF6A9ECA) else Color(0xFF1E5E99)
    AnnotationColor.PINK -> if (isDark) Color(0xFFCA7AA0) else Color(0xFF9E2060)
}
