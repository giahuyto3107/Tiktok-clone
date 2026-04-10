package com.example.tiktok_clone.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// TikTok "Dark Mode" (Default for Video Feed)
private val DarkColorScheme = darkColorScheme(
    primary = TikTokRed,
    onPrimary = White,
    secondary = BlueAccent,
    tertiary = YellowSave,
    background = Black,
    onBackground = TextOnDark, // Soft white
    surface = DarkSurface, // Dark Gray for Bottom Sheets
    onSurface = White,
    error = TikTokRed
)

// Light Mode (For Profile/Inbox if following system theme)
private val LightColorScheme = lightColorScheme(
    primary = TikTokRed,
    onPrimary = White,
    secondary = TextSecondary,
    tertiary = BlueAccent,
    background = White,
    onBackground = TextOnLight, // Black
    surface = White,
    onSurface = Black,
    surfaceVariant = GrayBackground, // Search bars
    error = TikTokRed
)

@Composable
fun Tiktok_cloneTheme(
    darkTheme: Boolean = false,
    // TikTok rarely uses Material Dynamic Colors (Wallpaper theming),
    // so we default this to false to maintain Brand Identity.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // TikTok status bar is usually transparent or follows the background
            window.statusBarColor = Color.Transparent.toArgb()

            // If we are in dark mode (or viewing video feed), icons should be light
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}