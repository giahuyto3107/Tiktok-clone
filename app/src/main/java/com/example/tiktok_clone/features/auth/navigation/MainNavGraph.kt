package com.example.tiktok_clone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiktok_clone.features.auth.ui.SearchResultScreen
import com.example.tiktok_clone.features.auth.ui.SearchScreen


@Composable
fun MainNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {

        composable("search") {
            SearchScreen(
                onNavigateToResult = { query ->
                    navController.navigate("search_result/$query")
                }
            )
        }

        composable("search_result/{query}") {
            SearchResultScreen(
                query = it.arguments?.getString("query") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
    }
}