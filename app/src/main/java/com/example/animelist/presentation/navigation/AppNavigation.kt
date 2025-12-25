// navigation/AppNavigation.kt
package com.example.animelist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animelist.presentation.screen.detail.AnimeDetailScreen
import com.example.animelist.presentation.screen.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                },
                onSearchClick = { /* TODO */ }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("animeId") { type = NavType.IntType }
            )
        ) {
            AnimeDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}