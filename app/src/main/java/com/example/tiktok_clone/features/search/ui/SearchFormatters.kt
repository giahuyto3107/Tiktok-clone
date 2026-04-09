package com.example.tiktok_clone.features.search.ui

import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun formatVideoDuration(totalSec: Int): String {
    if (totalSec <= 0) return "0:00"
    val m = totalSec / 60
    val s = totalSec % 60
    return String.format(Locale.US, "%d:%02d", m, s)
}

fun formatCompactCount(n: Int): String {
    return when {
        n >= 1_000_000 -> String.format(Locale.US, "%.1fM", n / 1_000_000.0)
        n >= 1_000 -> String.format(Locale.US, "%.1fK", n / 1_000.0)
        else -> n.toString()
    }
}

/** Thời gian tương đối tiếng Việt (createdAt = epoch ms). */
fun formatRelativePastVi(createdAtMs: Long?, nowMs: Long = System.currentTimeMillis()): String {
    if (createdAtMs == null) return ""
    val diff = abs(nowMs - createdAtMs)
    val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
    if (mins < 1) return "Vừa xong"
    if (mins < 60) return "$mins phút trước"
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    if (hours < 24) return "$hours giờ trước"
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    if (days < 30) return "$days ngày trước"
    val months = days / 30
    if (months < 12) return "$months tháng trước"
    return "${days / 365} năm trước"
}
