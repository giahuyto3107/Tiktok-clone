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
        fontSize = Dimens.FontTitleM, // 28sp
        lineHeight = 34.sp
    ),

    // Main Page Headlines
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = Dimens.FontTitleS, // 24sp
        lineHeight = 30.sp
    ),

    // Video Feed: Username / Profile Header
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimens.FontSizeXXL, // 20sp
        lineHeight = 28.sp
    ),

    // Standard list titles / Post Descriptions
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = Dimens.FontSizeL, // 16sp
        lineHeight = 24.sp
    ),

    // Video Feed: Captions / Description Body
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.FontSizeL, // 16sp
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Standard body text
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.FontSizeM, // 14sp
        lineHeight = 20.sp
    ),

    // Buttons and Tab Labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimens.FontSizeM, // 14sp
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Interaction Counters (Likes/Comments text)
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = Dimens.FontSizeS, // 12sp
        lineHeight = 16.sp
    ),

    // Tiny metadata or timestamps
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.FontSizeXS, // 10sp
        lineHeight = 14.sp
    )
)