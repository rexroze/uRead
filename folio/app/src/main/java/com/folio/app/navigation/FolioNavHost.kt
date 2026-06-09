package com.folio.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folio.app.feature.browse.BrowseScreen
import com.folio.app.feature.details.DetailsScreen
import com.folio.app.feature.home.HomeScreen
import com.folio.app.feature.library.LibraryScreen
import com.folio.app.feature.reader.ReaderScreen

@Composable
fun FolioNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.HOME.route,
    ) {
        // ---- Tabs: cross-fade, no motion noise. ----
        composable(
            route = TopLevelDestination.HOME.route,
            enterTransition = { fadeIn(tween(180)) },
            exitTransition = { fadeOut(tween(180)) },
        ) {
            HomeScreen(
                onOpen = { id -> navController.navigate(Routes.details(id)) },
                onContinue = { id -> navController.navigate(Routes.reader(id)) },
            )
        }
        composable(
            route = TopLevelDestination.LIBRARY.route,
            enterTransition = { fadeIn(tween(180)) },
            exitTransition = { fadeOut(tween(180)) },
        ) {
            LibraryScreen(onOpen = { id -> navController.navigate(Routes.details(id)) })
        }
        composable(
            route = TopLevelDestination.BROWSE.route,
            enterTransition = { fadeIn(tween(180)) },
            exitTransition = { fadeOut(tween(180)) },
        ) {
            BrowseScreen()
        }

        // ---- Detail: slide in from the side. ----
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument(Routes.MEDIA_ID_ARG) { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(tween(220)) { it / 4 } + fadeIn(tween(220)) },
            exitTransition = { fadeOut(tween(160)) },
            popExitTransition = { slideOutHorizontally(tween(200)) { it / 4 } + fadeOut(tween(200)) },
        ) { entry ->
            val id = entry.arguments?.getString(Routes.MEDIA_ID_ARG).orEmpty()
            DetailsScreen(
                mediaId = id,
                onBack = { navController.popBackStack() },
                onRead = { navController.navigate(Routes.reader(id)) },
            )
        }

        // ---- Reader: full-bleed, fade only. ----
        composable(
            route = Routes.READER,
            arguments = listOf(navArgument(Routes.MEDIA_ID_ARG) { type = NavType.StringType }),
            enterTransition = { fadeIn(tween(220)) },
            exitTransition = { fadeOut(tween(160)) },
        ) { entry ->
            val id = entry.arguments?.getString(Routes.MEDIA_ID_ARG).orEmpty()
            ReaderScreen(
                mediaId = id,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

/** Standard "switch tab" semantics: single-top, restore state, pop to start. */
fun NavController.navigateToTopLevel(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
