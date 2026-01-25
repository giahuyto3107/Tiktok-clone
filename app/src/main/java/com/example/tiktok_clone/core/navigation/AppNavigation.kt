package com.example.tiktok_clone.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiktok_clone.features.home.ui.CameraAccessScreen
import com.example.tiktok_clone.features.home.ui.HomeScreen
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            HomeScreen(
                onNavigationToCameraAccessScreen = {navController.navigate("camera_access")}
            )
        }

        composable(route = "camera_access") {
            CameraAccessScreen(
                onNavigationToHomeScreen = {navController.navigate("home")}
            )
        }
    }
}