package com.example.tiktok_clone.features.social.ui.components

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateString(): String {
    val createdAtMillis = if (this < 1_000_000_000_000L) this * 1000 else this
    val now = System.currentTimeMillis()
    val diff = (now - createdAtMillis).coerceAtLeast(0L)

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7

    return when {
        seconds < 5 -> "vừa xong"
        seconds < 60 -> "$seconds giây"
        minutes < 60 -> "$minutes phút"
        hours < 24 -> "$hours giờ"
        days < 7 -> "$days ngày"
        weeks < 4 -> "$weeks tuần"
        days < 365 -> SimpleDateFormat("dd-MM", Locale.getDefault()).format(Date(createdAtMillis))
        else -> SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(createdAtMillis))
    }
}
