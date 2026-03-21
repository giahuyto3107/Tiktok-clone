package com.example.tiktok_clone.features.social.ui.components

// định dạng đếm số
fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> "%.1fTr".format(count / 1_000_000.0)
        count >= 100_000 -> "%1.1fK".format(count / 1_000.0)
        count >= 10_000 -> "%.1fN".format(count / 1_000.0)
        count >= 1_000 -> "%.3f".format(count / 1_000.0)
        else -> count.toString()
    }
}