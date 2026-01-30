package com.example.tiktok_clone.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiktok_clone.features.home.ui.camera.CameraAccessScreen
import com.example.tiktok_clone.features.home.ui.home.HomeScreen
import com.example.tiktok_clone.features.profile.ui.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
//        composable (route = "profile"){
//            LoginScreen(
//                onNavigateToLoginScreen = {navController.navigate("login")}
//            )
//        }
//
//        composable (route = "login"){
//            LoginScreen(
//                onNavigationToProfileScreen = {navController.navigate("profile")}
//            )
//        }

        composable (route = "home"){
            HomeScreen(
                onNavigationToProfileScreen = {navController.navigate("profile")}
            )
        }
        composable(route = "profile") {
            ProfileScreen(
                onNavigationToProfileScreen = {navController.navigate("home")}
            )
        }
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