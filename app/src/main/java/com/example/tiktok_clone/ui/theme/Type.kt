package com.example.tiktok_clone.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // Large Hero Titles (e.g., Profile Names or Headers)
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp, // font_title_m
        lineHeight = 36.sp
    ),

    // Main Page Headlines
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp, // font_title_s
        lineHeight = 32.sp
    ),

    // Video Feed: Username
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, // font_xxl
        lineHeight = 28.sp
    ),

    // Video Feed: Captions / Description
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp, // font_l
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Buttons and Tab Labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp, // font_m
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Interaction Counters (Likes/Comments)
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp, // font_s
        lineHeight = 16.sp
    ),

    // Tiny metadata or timestamps
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp, // font_xs
        lineHeight = 14.sp
    )
)