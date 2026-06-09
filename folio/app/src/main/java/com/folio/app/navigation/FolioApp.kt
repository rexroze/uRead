package com.folio.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.folio.app.core.designsystem.theme.FolioTheme

/**
 * Root shell: hosts the nav graph and shows the bottom bar only on the three
 * top-level tabs (it hides itself inside details and the reader so reading is
 * full-bleed).
 */
@Composable
fun FolioApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()

    val topLevel = TopLevelDestination.entries.firstOrNull { dest ->
        backStack?.destination?.hierarchy?.any { it.route == dest.route } == true
    }
    val showBar = topLevel != null

    Scaffold(
        containerColor = FolioTheme.colors.paper,
        bottomBar = {
            if (showBar) {
                FolioBottomBar(
                    current = topLevel ?: TopLevelDestination.HOME,
                    onSelect = { dest -> navController.navigateToTopLevel(dest) },
                )
            }
        },
    ) { innerPadding ->
        // The reader/details consume insets themselves; tabs use the padding.
        val padding = if (showBar) innerPadding else PaddingValues(0.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FolioNavHost(navController = navController)
        }
    }
}
