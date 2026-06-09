package com.folio.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Top-level tabs. Folio keeps to three so the bottom bar stays calm:
 * Home (continue), Library (everything you own), Browse (find more).
 */
enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("home", "Home", Icons.Outlined.AutoStories),
    LIBRARY("library", "Library", Icons.Outlined.GridView),
    BROWSE("browse", "Browse", Icons.Outlined.Explore),
}

/** Non-tab routes. */
object Routes {
    const val DETAILS = "details/{mediaId}"
    fun details(mediaId: String) = "details/$mediaId"

    const val READER = "reader/{mediaId}"
    fun reader(mediaId: String) = "reader/$mediaId"

    const val STATISTICS = "statistics"
    const val SETTINGS = "settings"

    const val MEDIA_ID_ARG = "mediaId"
}
