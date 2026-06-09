package com.folio.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.folio.app.core.designsystem.theme.FolioTheme
import com.folio.app.core.designsystem.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolioMenuSheet(
    onDismiss: () -> Unit,
    onStatistics: () -> Unit,
    onSettings: () -> Unit,
) {
    val colors = FolioTheme.colors
    val sheetState = rememberModalBottomSheetState(skipPartialExpansion = true)

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
                .padding(horizontal = Spacing.gutter)
                .padding(bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Text(
                text = "Folio",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.ink,
                modifier = Modifier.padding(bottom = Spacing.lg),
            )

            MenuRow(
                icon = Icons.Outlined.BarChart,
                label = "Statistics",
                onClick = { onStatistics(); onDismiss() },
            )
            MenuRow(
                icon = Icons.Outlined.Settings,
                label = "Settings",
                onClick = { onSettings(); onDismiss() },
            )
            Spacer(Modifier.height(Spacing.sm))
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    val colors = FolioTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Icon(icon, contentDescription = label, tint = colors.inkMuted, modifier = Modifier.size(22.dp))
        Text(label, style = MaterialTheme.typography.titleMedium, color = colors.ink)
    }
}
