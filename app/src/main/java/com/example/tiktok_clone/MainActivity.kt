package com.example.tiktok_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tiktok_clone.core.navigation.AppNavigation
import com.example.tiktok_clone.features.home.ui.home.HomeScreen
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
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {

    HomeScreen()
}