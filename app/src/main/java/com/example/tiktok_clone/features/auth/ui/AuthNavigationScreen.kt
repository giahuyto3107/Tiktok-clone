package com.example.tiktok_clone.features.auth.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.tiktok.features.auth.screens.LoginFormScreen
import com.example.tiktok.features.auth.screens.SignUpFormScreen
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController

// Định nghĩa đầy đủ 4 trạng thái màn hình
enum class AuthScreenState {
    SELECT_SIGNUP, // 1. Chọn phương thức Đăng ký
    SIGNUP_FORM,   // 2. Nhập Form Đăng ký (SĐT/OTP)
    SELECT_LOGIN,  // 3. Chọn phương thức Đăng nhập
    LOGIN_FORM     // 4. Nhập Form Đăng nhập (Pass/OTP) - MỚI THÊM
}

@Composable
fun TikTokAuthNavigation(navController: NavHostController,
                         onLoginSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthHelper = remember { GoogleAuthUiHelper(context) }

    var currentScreen by remember { mutableStateOf(AuthScreenState.SELECT_SIGNUP) }

    when (currentScreen) {
        // --- 1. CHỌN ĐĂNG KÝ ---
        AuthScreenState.SELECT_SIGNUP -> {
            SelectSignUpScreen(
                onPhoneEmailClick = { currentScreen = AuthScreenState.SIGNUP_FORM },
                onLoginClick = { currentScreen = AuthScreenState.SELECT_LOGIN },
                onGoogleClick = {
                    coroutineScope.launch {
                        val result = googleAuthHelper.signInGoogle()
                        result.onSuccess { user ->
//                            onGoogle
                        }.onFailure { e ->
                            Toast.makeText(context, "Sign in Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        // --- 2. FORM ĐĂNG KÝ ---
        AuthScreenState.SIGNUP_FORM -> {
            SignUpFormScreen(
                onBack = { currentScreen = AuthScreenState.SELECT_SIGNUP },
                onLoginClick = { currentScreen = AuthScreenState.SELECT_LOGIN }
            )
        }

        // --- 3. CHỌN ĐĂNG NHẬP ---
        AuthScreenState.SELECT_LOGIN -> {
            LoginSelectionScreen(
                onSignUpClick = { currentScreen = AuthScreenState.SELECT_SIGNUP },
                onBack = { currentScreen = AuthScreenState.SELECT_SIGNUP },
                onPhoneEmailLoginClick = { currentScreen = AuthScreenState.LOGIN_FORM },
                onLoginSuccess = {
                    // Bây giờ bạn đã có thể gọi navController vì nó đã được truyền vào hàm
                    navController.navigate("main_wrapper") {
                        popUpTo("login_selection") { inclusive = true }
                    }
                }
            )
        }

        // --- 4. FORM ĐĂNG NHẬP ---
        AuthScreenState.LOGIN_FORM -> {
            LoginFormScreen(
                onBack = { currentScreen = AuthScreenState.SELECT_LOGIN },
                onSignUpClick = { currentScreen = AuthScreenState.SELECT_SIGNUP },
                onLoginSuccess = onLoginSuccess

            )
        }
    }
}