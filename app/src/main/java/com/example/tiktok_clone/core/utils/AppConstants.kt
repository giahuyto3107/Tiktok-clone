package com.example.tiktok_clone.core.utils

import java.time.LocalDate
import java.time.LocalDateTime

object AppConstants {

    // ============ Spacing ============
    const val SPACING_XXS = 2.0
    const val SPACING_XS = 4.0
    const val SPACING_S = 8.0
    const val SPACING_M = 12.0
    const val SPACING_L = 16.0
    const val SPACING_XL = 24.0
    const val SPACING_XXL = 32.0
    const val SPACING_XXXL = 48.0
    const val SPACING_NAVIGATION_BAR = 100.0

    // ============ Border Radius ============
    const val RADIUS_S = 4.0
    const val RADIUS_M = 8.0
    const val RADIUS_L = 12.0
    const val RADIUS_XL = 16.0
    const val RADIUS_XXL = 24.0
    const val RADIUS_CIRCULAR = 999.0

    // ============ Icon Sizes ============
    const val ICON_XS = 16.0
    const val ICON_S = 20.0
    const val ICON_M = 24.0
    const val ICON_L = 32.0
    const val ICON_XL = 48.0
    const val ICON_XXL = 64.0

    // ============ Font Sizes ============
    const val FONT_XS = 10.0
    const val FONT_S = 12.0
    const val FONT_M = 14.0
    const val FONT_L = 16.0
    const val FONT_XL = 18.0
    const val FONT_XXL = 20.0
    const val FONT_XXXL = 22.0
    const val FONT_TITLE_S = 24.0
    const val FONT_TITLE_M = 28.0
    const val FONT_TITLE_L = 32.0

    // ============ Elevation/Shadow ============
    const val ELEVATION_S = 2.0
    const val ELEVATION_M = 4.0
    const val ELEVATION_L = 8.0
    const val ELEVATION_XL = 16.0

    // ============ Border Width ============
    const val BORDER_THIN = 1.0
    const val BORDER_MEDIUM = 2.0
    const val BORDER_THICK = 3.0

    // ============ Button/Card Sizes ============
    const val BUTTON_HEIGHT_S = 36.0
    const val BUTTON_HEIGHT_M = 48.0
    const val BUTTON_HEIGHT_L = 56.0
    const val BUTTON_PADDING_H = 24.0
    const val CARD_HEIGHT_S = 80.0
    const val CARD_HEIGHT_M = 120.0
    const val CARD_HEIGHT_L = 180.0
    const val CARD_PADDING = 16.0

    // ============ AppBar & Nav ============
    const val APP_BAR_HEIGHT = 56.0
    const val APP_BAR_HEIGHT_LARGE = 128.0
    const val BOTTOM_NAV_HEIGHT = 60.0

    // ============ Durations & Logic ============
    const val ANIMATION_SHORT = 200
    const val ANIMATION_MEDIUM = 300
    const val ANIMATION_LONG = 500
    const val RESEND_TIME_COUNTDOWN = 15
    const val REQUEST_TIMEOUT = 30L
    const val MAX_RETRIES = 3
    const val ITEMS_PER_PAGE = 20
    const val LOAD_MORE_THRESHOLD = 5

    // ============ Database ============
    const val DATABASE_NAME = "nutripal.db"
    const val DATABASE_VERSION = 1

    // ============ Date/Time (Works on API 24 with Desugaring) ============
    const val DATE_FORMAT_FULL = "dd/MM/yyyy"
    const val DATE_FORMAT_SHORT = "dd/MM"
    const val TIME_FORMAT_24 = "HH:mm"
    const val TIME_FORMAT_12 = "hh:mm a"
    const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"

    val LAST_DATE_DATE_PICKER: Int get() = LocalDate.now().year + 1
    val SHORT_TIME_SPAN_START: LocalDateTime get() = LocalDateTime.now().minusDays(55)
    val SHORT_TIME_SPAN_END: LocalDateTime get() = LocalDateTime.now()

    // ============ Regex & Assets ============
    const val EMAIL_PATTERN = """^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$"""
    const val PHONE_PATTERN = """^(0|\+84)(\d{9,10})$"""
    const val BACKGROUND_IMAGE_PATH = "assets/home_background.png"
}