package com.example.tiktok_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.tiktok_clone.features.auth.ui.SearchScreen
import com.example.tiktok_clone.ui.theme.Tiktok_cloneTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 👉 Cho phép edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 👉 Set màu status bar (PIN + TAI THỎ RÕ)
        window.statusBarColor = Color.White.toArgb()

        // 👉 Icon pin, sóng màu ĐEN
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = true

        setContent {
            Tiktok_cloneTheme {
                SearchScreen()
            }
        }
    }
}
