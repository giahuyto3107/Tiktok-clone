package com.example.tiktok_clone.features.auth.ui

import androidx.compose.runtime.*
import com.example.tiktok.features.auth.screens.LoginFormScreen
import com.example.tiktok.features.auth.screens.LoginSelectionScreen
import com.example.tiktok.features.auth.screens.SignUpFormScreen

// Định nghĩa đầy đủ 4 trạng thái màn hình
enum class AuthScreenState {
    SELECT_SIGNUP, // 1. Chọn phương thức Đăng ký
    SIGNUP_FORM,   // 2. Nhập Form Đăng ký (SĐT/OTP)
    SELECT_LOGIN,  // 3. Chọn phương thức Đăng nhập
    LOGIN_FORM     // 4. Nhập Form Đăng nhập (Pass/OTP) - MỚI THÊM
}

@Composable
fun TikTokAuthNavigation() {
    var currentScreen by remember { mutableStateOf(AuthScreenState.SELECT_SIGNUP) }

    when (currentScreen) {
        // --- 1. CHỌN ĐĂNG KÝ ---
        AuthScreenState.SELECT_SIGNUP -> {
            SelectSignUpScreen(
                onPhoneEmailClick = { currentScreen = AuthScreenState.SIGNUP_FORM },
                onLoginClick = { currentScreen = AuthScreenState.SELECT_LOGIN }
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
                onPhoneEmailLoginClick = {
                    // ĐÃ FIX: Chuyển sang màn hình nhập mật khẩu/OTP
                    currentScreen = AuthScreenState.LOGIN_FORM
                }
            )
        }

        // --- 4. FORM ĐĂNG NHẬP ---
        AuthScreenState.LOGIN_FORM -> {
            LoginFormScreen(
                onBack = { currentScreen = AuthScreenState.SELECT_LOGIN },
                onSignUpClick = { currentScreen = AuthScreenState.SELECT_SIGNUP }
            )
        }
    }
}