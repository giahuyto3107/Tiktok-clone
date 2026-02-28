package com.example.tiktok_clone.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiktok.features.auth.screens.LoginFormScreen
import com.example.tiktok_clone.features.auth.ui.LoginSelectionScreen
import com.example.tiktok.features.auth.screens.SignUpFormScreen
import com.example.tiktok_clone.core.ui.MainWrapper
import com.example.tiktok_clone.features.auth.ui.AuthenticatedProfile
import com.example.tiktok_clone.features.auth.ui.SelectSignUpScreen
import com.example.tiktok_clone.features.auth.ui.TikTokAuthNavigation
import com.example.tiktok_clone.features.search.ui.SearchScreen
import com.example.tiktok_clone.features.home.camera.ui.CameraAccessScreen
import com.example.tiktok_clone.features.home.home.ui.HomeScreen
import com.example.tiktok_clone.features.inbox.ui.InboxScreen
import com.example.tiktok_clone.features.profile.ui.ProfileScreen
import com.example.tiktok_clone.features.search.ui.SearchResultScreen
import com.example.tiktok_clone.features.shop.ui.ShopScreen
import com.google.firebase.auth.FirebaseAuth

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
                    4 -> ProfileScreenContent(
                        onLoginClick = {
                            navController.navigate("login_selection")
                        }
                    )
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

        composable(route = "login_selection") {
            LoginSelectionScreen(
                onBack = { navController.popBackStack() },
                onSignUpClick = { navController.navigate("select_signup") },
                onPhoneEmailLoginClick = { navController.navigate("login_form") },
                onLoginSuccess = {
                    // 1. Reset ngay lập tức biến index về 0 (Home)
                    selectedTabIndex = 0

                    // 2. Điều hướng về Wrapper và dọn dẹp Backstack
                    navController.navigate("main_wrapper") {
                        // Ta popUpTo về tận main_wrapper để đảm bảo stack sạch sẽ
                        popUpTo("main_wrapper") { inclusive = false }

                        // Tránh việc mở chồng nhiều màn hình MainWrapper
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = "select_signup") {
            SelectSignUpScreen(
                onPhoneEmailClick = { navController.navigate("signup_form") },
                onLoginClick = {
                    // Quay lại màn hình login selection hoặc pop back
                    navController.navigate("login_selection") {
                        popUpTo("login_selection") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "signup_form") {
            SignUpFormScreen(
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate("login_selection") {
                        popUpTo("login_selection") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "login_form") {
            LoginFormScreen(
                onBack = { navController.popBackStack() },
                onSignUpClick = { navController.navigate("select_signup") },
                onLoginSuccess = {
                    selectedTabIndex = 0
                    navController.navigate("main_wrapper") {
                        popUpTo("login_selection") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    onSearchTap: () -> Unit = {},
) {
    HomeScreen(
        onSearchTap = onSearchTap
    )
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
private fun ProfileScreenContent(onLoginClick: () -> Unit) {
    // Kiểm tra trạng thái đăng nhập từ Firebase (reactive)
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(listener)
        }
    }

    if (currentUser != null) {
        // Đã đăng nhập: Hiện giao diện Profile thật
        AuthenticatedProfile(onLogout = {
            // Khi logout, ta có thể reset tab hoặc quay lại màn hình chọn login
            onLoginClick()
        })
    } else {
        // Chưa đăng nhập: Hiện màn hình yêu cầu Login như cũ
        ProfileScreen(onLoginClick = onLoginClick)
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}