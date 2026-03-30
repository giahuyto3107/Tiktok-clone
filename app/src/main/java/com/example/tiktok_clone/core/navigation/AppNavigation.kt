package com.example.tiktok_clone.core.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiktok.features.auth.screens.LoginFormScreen
import com.example.tiktok.features.auth.screens.SignUpFormScreen
import com.example.tiktok_clone.core.ui.MainWrapper
import com.example.tiktok_clone.features.auth.ui.AuthenticatedProfile
import com.example.tiktok_clone.features.auth.ui.LoginSelectionScreen
import com.example.tiktok_clone.features.auth.ui.SelectSignUpScreen
import com.example.tiktok_clone.features.camera.ui.CameraAccessScreen
import com.example.tiktok_clone.features.home.ui.HomeScreen
import com.example.tiktok_clone.features.inbox.ui.chatState.MessageScreen
import com.example.tiktok_clone.features.inbox.ui.inboxState.InboxScreen
import com.example.tiktok_clone.features.post.ui.PrePostScreen
import com.example.tiktok_clone.features.post.ui.PreviewScreen
import com.example.tiktok_clone.features.profile.ui.ProfileScreen
import com.example.tiktok_clone.features.search.ui.SearchResultScreen
import com.example.tiktok_clone.features.search.ui.SearchScreen
import com.example.tiktok_clone.features.shop.ui.ShopScreen
import com.example.tiktok_clone.features.notification.ui.Notification

import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.mainWrapper,
    ) {
        composable(route = NavigationRoutes.mainWrapper) {
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
                    navController.navigate(NavigationRoutes.cameraAccessRoute)
                }
            ) {
                // Content based on selected tab
                when (selectedTabIndex) {
                    0 -> HomeScreenContent(
                        onsearchTap = { navController.navigate(NavigationRoutes.searchRoute) }
                    )
                    1 -> ShopScreenContent()
                    3 -> InboxScreenContent(
                        onChatClick = { userId ->
                            navController.navigate("${NavigationRoutes.inboxRoute}/$userId")
                        },
                        onUserNotificationClick = {
                            navController.navigate(NavigationRoutes.notificationUserRoute)
                        },
                        onSocialNotificationClick = {
                            navController.navigate(NavigationRoutes.notificationSocialRoute)
                        },
                    )
                    4 -> ProfileScreenContent(
                        onLoginClick = {
                            navController.navigate(NavigationRoutes.loginSelectionRoute)
                        }
                    )
                }
            }
        }

        composable(route = NavigationRoutes.cameraAccessRoute) {
            CameraAccessScreen(
                onNavigationToHomeScreen = {
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo(NavigationRoutes.mainWrapper) { inclusive = true }
                    }
                },
                onNavigateToPreview = { uriString, type ->
                    val encodedUri = Uri.encode(uriString)
                    navController.navigate("preview_screen/$encodedUri/$type")
                }
            )
        }

        composable(
            route = NavigationRoutes.PREVIEW_SCREEN_ROUTE,
            arguments = listOf(
                navArgument("mediaUri") { type = NavType.StringType },
                navArgument("postType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("mediaUri") ?: ""
            val postType = backStackEntry.arguments?.getString("postType") ?: "IMAGE"

            val decodedUri = Uri.decode(encodedUri)

            PreviewScreen(
                mediaUri = decodedUri,
                postType = postType,
                onBack = { navController.popBackStack() },
                onNext = { uriString, type ->
                    val encodedUri = Uri.encode(uriString)
                    navController.navigate("pre_post_screen/$encodedUri/$type")
                },
            )
        }

        composable(
            route = NavigationRoutes.prePostScreenRoute,
            arguments = listOf(
                navArgument("mediaUri") { type = NavType.StringType },
                navArgument("postType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("mediaUri") ?: ""
            val postType = backStackEntry.arguments?.getString("postType") ?: "IMAGE"

            val decodedUri = Uri.decode(encodedUri)

            PrePostScreen(
                onBack = { navController.popBackStack() },
                onPostSuccess = {
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo(NavigationRoutes.mainWrapper) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                mediaUri = decodedUri,
                postType = postType
            )
        }

        composable(NavigationRoutes.searchRoute) {
            SearchScreen(
                onNavigateToResult = { query ->
                    navController.navigate("${NavigationRoutes.searchResultRoute}/$query")
                }
            )
        }

        composable("${NavigationRoutes.searchResultRoute}/{query}") {
            SearchResultScreen(
                query = it.arguments?.getString("query") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = NavigationRoutes.loginSelectionRoute) {
            LoginSelectionScreen(
                onBack = { navController.popBackStack() },
                onSignUpClick = { navController.navigate(NavigationRoutes.selectSignUpRoute) },
                onPhoneEmailLoginClick = { navController.navigate(NavigationRoutes.loginFormRoute) },
                onLoginSuccess = {
                    // 1. Reset ngay lập tức biến index về 0 (Home)
                    selectedTabIndex = 0

                    // 2. Điều hướng về Wrapper và dọn dẹp Backstack
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        // Ta popUpTo về tận main_wrapper để đảm bảo stack sạch sẽ
                        popUpTo(NavigationRoutes.mainWrapper) { inclusive = false }

                        // Tránh việc mở chồng nhiều màn hình MainWrapper
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = NavigationRoutes.selectSignUpRoute) {
            SelectSignUpScreen(
                onPhoneEmailClick = { navController.navigate(NavigationRoutes.signUpFormRoute) },
                onLoginClick = {
                    // Quay lại màn hình login selection hoặc pop back
                    navController.navigate(NavigationRoutes.loginSelectionRoute) {
                        popUpTo(NavigationRoutes.loginSelectionRoute) { inclusive = true }
                    }
                }
            )
        }

        composable(route = NavigationRoutes.signUpFormRoute) {
            SignUpFormScreen(
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(NavigationRoutes.loginSelectionRoute) {
                        popUpTo(NavigationRoutes.loginSelectionRoute) { inclusive = true }
                    }
                }
            )
        }

        composable(route = NavigationRoutes.loginFormRoute) {
            LoginFormScreen(
                onBack = { navController.popBackStack() },
                onSignUpClick = { navController.navigate(NavigationRoutes.selectSignUpRoute) },
                onLoginSuccess = {
                    selectedTabIndex = 0
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo(NavigationRoutes.loginSelectionRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable (route = NavigationRoutes.inboxRoute){
            InboxScreen(
                onChatClick = { userId ->
                    navController.navigate("${NavigationRoutes.inboxRoute}/$userId")
                },
                onUserNotificationClick = {
                    navController.navigate(NavigationRoutes.notificationUserRoute)
                },
                onSocialNotificationClick = {
                    navController.navigate(NavigationRoutes.notificationSocialRoute)
                },
            )
        }
        composable (route = "${NavigationRoutes.inboxRoute}/{userId}"){
            MessageScreen(
                chatWithId = it.arguments?.getString("userId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = NavigationRoutes.notificationSocialRoute){
            Notification(
                notificationType = "social",
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = NavigationRoutes.notificationUserRoute){
            Notification(
                notificationType = "user",
                onBack = { navController.popBackStack() }
            )
        }

    }
}

@Composable
private fun HomeScreenContent(
    onsearchTap: () -> Unit = {},
) {
    HomeScreen(
        onSearchTap = onsearchTap
    )
}

@Composable
private fun ShopScreenContent() {
    ShopScreen()
}

@Composable
private fun InboxScreenContent(
    onChatClick: (userId: String) -> Unit = {},
    onUserNotificationClick: () -> Unit = {},
    onSocialNotificationClick: () -> Unit = {},
) {
    InboxScreen(
        onChatClick = onChatClick,
        onUserNotificationClick = onUserNotificationClick,
        onSocialNotificationClick = onSocialNotificationClick,
    )
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