package com.folio.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.folio.app.core.designsystem.theme.FolioTheme

/**
 * Root shell: hosts the nav graph, shows the bottom bar on the three top-level
 * tabs, and manages the hamburger menu sheet that surfaces Statistics and Settings.
 */
@Composable
fun FolioApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    var menuVisible by remember { mutableStateOf(false) }

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
        val padding = if (showBar) innerPadding else PaddingValues(0.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FolioNavHost(
                navController = navController,
                onMenuOpen = { menuVisible = true },
            )
        }
    }

    if (menuVisible) {
        FolioMenuSheet(
            onDismiss = { menuVisible = false },
            onStatistics = { navController.navigate(Routes.STATISTICS) },
            onSettings = { navController.navigate(Routes.SETTINGS) },
        )
    }
}
