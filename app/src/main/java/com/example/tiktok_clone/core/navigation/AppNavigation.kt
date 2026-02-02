package com.example.tiktok_clone.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiktok_clone.core.ui.MainWrapper
import com.example.tiktok_clone.features.search.ui.SearchScreen
import com.example.tiktok_clone.features.home.ui.camera.CameraAccessScreen
import com.example.tiktok_clone.features.home.ui.home.HomeScreen
import com.example.tiktok_clone.features.inbox.ui.InboxScreen
import com.example.tiktok_clone.features.profile.ui.ProfileScreen
import com.example.tiktok_clone.features.shop.ui.ShopScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = "main_wrapper",
    ) {
        composable(route = "main_wrapper") {
            MainWrapper(
                selectedIndex = selectedTabIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    // Navigate to the appropriate screen based on index
                    when (index) {
                        0 -> {} // Home - already here
                        1 -> {} // Shop - handled by content
                        3 -> {} // Inbox - handled by content
                        4 -> {} // Profile - handled by content
                    }
                },
                onCameraClick = {
                    navController.navigate("camera_access")
                }
            ) {
                // Content based on selected tab
                when (selectedTabIndex) {
                    0 -> HomeScreenContent(
                        onSearchTap = { navController.navigate("search") }
                    )
                    1 -> ShopScreenContent()
                    3 -> InboxScreenContent()
                    4 -> ProfileScreenContent()
                }
            }
        }

        composable(route = "camera_access") {
            CameraAccessScreen(
                onNavigationToHomeScreen = {
                    navController.navigate("main_wrapper") {
                        popUpTo("main_wrapper") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "search") {
            SearchScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    onSearchTap: () -> Unit = {}
) {
    HomeScreen(onSearchTap = onSearchTap)
}

@Composable
private fun ShopScreenContent() {
    ShopScreen()
}

@Composable
private fun InboxScreenContent() {
    InboxScreen()
}

@Composable
private fun ProfileScreenContent() {
    ProfileScreen()
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}