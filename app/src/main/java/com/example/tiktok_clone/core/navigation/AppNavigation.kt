package com.example.tiktok_clone.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tiktok.features.auth.screens.LoginFormScreen
import com.example.tiktok.features.auth.screens.SignUpFormScreen
import com.example.tiktok_clone.core.ui.MainWrapper
import com.example.tiktok_clone.features.auth.ui.AuthenticatedProfile
import com.example.tiktok_clone.features.auth.ui.LoginSelectionScreen
import com.example.tiktok_clone.features.auth.ui.SelectSignUpScreen
import com.example.tiktok_clone.features.camera.ui.CameraAccessScreen
import com.example.tiktok_clone.features.home.ui.HomeScreen
import com.example.tiktok_clone.features.inbox.ui.message.MessageScreen
import com.example.tiktok_clone.features.inbox.ui.inbox.InboxScreen
import com.example.tiktok_clone.features.post.ui.PrePostScreen
import com.example.tiktok_clone.features.post.ui.PreviewScreen
import com.example.tiktok_clone.features.profile.ui.ProfileScreen
import com.example.tiktok_clone.features.search.SearchViewModel
import com.example.tiktok_clone.features.search.ui.SearchResultScreen
import com.example.tiktok_clone.features.search.ui.SearchScreen
import com.example.tiktok_clone.features.shop.ui.ShopScreen
import com.example.tiktok_clone.features.notification.ui.Notification
import com.example.tiktok_clone.features.profile.ui.OtherUserProfileScreen

import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.mainWrapper,
    ) {
        composable(route = NavigationRoutes.mainWrapper) { backStackEntry ->
            val homeViewModel: com.example.tiktok_clone.features.home.viewmodel.HomeViewModel = koinViewModel()
            val shouldRefresh = backStackEntry.savedStateHandle.get<Boolean>("refresh_home") ?: false
            
            var homeClickCount by remember { mutableStateOf(0) }

            androidx.compose.runtime.LaunchedEffect(shouldRefresh) {
                if (shouldRefresh) {
                    homeViewModel.refreshPosts()
                    backStackEntry.savedStateHandle["refresh_home"] = false
                }
            }

            MainWrapper(
                selectedIndex = selectedTabIndex,
                onTabSelected = { index ->
                    if (selectedTabIndex == 0 && index == 0) {
                        homeClickCount++
                    }
                    selectedTabIndex = index
                },
                onCameraClick = {
                    navController.navigate(NavigationRoutes.cameraAccessRoute)
                }
            ) {
                // Content based on selected tab
                when (selectedTabIndex) {
                    0 -> HomeScreenContent(
                        homeClickCount = homeClickCount,
                        onsearchTap = { navController.navigate(NavigationRoutes.searchGraphRoute) },
                        onAvatarClick = { userId -> navController.navigate("user_profile/$userId") },
                        onCommentClick = { userId -> navController.navigate("user_profile/$userId") }
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
                        },
                        navController = navController
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
                    navController.getBackStackEntry(NavigationRoutes.mainWrapper)
                        .savedStateHandle["refresh_home"] = true
                    selectedTabIndex = 0
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo(NavigationRoutes.mainWrapper) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                mediaUri = decodedUri,
                postType = postType
            )
        }

        navigation(
            route = NavigationRoutes.searchGraphRoute,
            startDestination = NavigationRoutes.searchHomeRoute,
        ) {
            composable(NavigationRoutes.searchHomeRoute) {
                // Activity scope: tránh getBackStackEntry(search_flow) lỗi / crash khi vào graph
                val activity = LocalContext.current as ComponentActivity
                val searchVm: SearchViewModel = koinViewModel(viewModelStoreOwner = activity)
                SearchScreen(
                    viewModel = searchVm,
                    onBack = { navController.popBackStack() },
                    onNavigateToResult = {
                        navController.navigate(NavigationRoutes.searchResultRoute)
                    },
                )
            }
            composable(NavigationRoutes.searchResultRoute) {
                val activity = LocalContext.current as ComponentActivity
                val searchVm: SearchViewModel = koinViewModel(viewModelStoreOwner = activity)
                SearchResultScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = searchVm,
                    onAvatarClick = { userId -> navController.navigate("user_profile/$userId") },
                )
            }
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
        composable(route = NavigationRoutes.inboxRoute) {
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
        composable(route = "${NavigationRoutes.inboxRoute}/{userId}") {
            MessageScreen(
                chatWithId = it.arguments?.getString("userId") ?: "",
                onBack = {
                    selectedTabIndex = 3
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo("${NavigationRoutes.inboxRoute}/{userId}") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onAvatarClick = { userId -> navController.navigate("user_profile/$userId") }
            )
        }
        composable(route = NavigationRoutes.notificationSocialRoute) {
            Notification(
                notificationType = "social",
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = NavigationRoutes.notificationUserRoute) {
            Notification(
                notificationType = "user",
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "user_profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            OtherUserProfileScreen(
                userId = userId,
                onBack = {
                    navController.popBackStack()
                },
                onChatClick = { userId ->
                    navController.navigate("${NavigationRoutes.inboxRoute}/$userId")
                },
                onNavigateToSelfProfile = {
                    selectedTabIndex = 4 // chuyển về tab Profile
                    navController.navigate(NavigationRoutes.mainWrapper) {
                        popUpTo(NavigationRoutes.mainWrapper) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }

    }
}

@Composable
private fun HomeScreenContent(
    homeClickCount: Int = 0,
    onsearchTap: () -> Unit = {},
    onAvatarClick: (String) -> Unit = {},
    onCommentClick: (String) -> Unit = {}
) {
    HomeScreen(
        homeClickCount = homeClickCount,
        onSearchTap = onsearchTap,
        onAvatarClick = onAvatarClick,
        onCommentClick = onCommentClick
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
private fun ProfileScreenContent(
    onLoginClick: () -> Unit,
    navController: androidx.navigation.NavHostController,
    profileViewModel: com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel = koinViewModel()
) {
    // Kiểm tra trạng thái đăng nhập từ Firebase (reactive)
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            // Nếu có user mới (vừa sign in), ta trigger refresh profile data
            if (currentUser != null) {
                profileViewModel.refreshProfile()
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(listener)
        }
    }

    if (currentUser != null) {
        // Đã đăng nhập: Hiện giao diện Profile thật
        AuthenticatedProfile(onLogout = {
            // Khi logout, ta xóa listener hoặc force refresh local state
            currentUser = null
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