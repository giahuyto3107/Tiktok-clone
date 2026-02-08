package com.example.tiktok_clone.features.auth.model

data class SearchUiState(
    val query: String = "",
    val suggestions: List<String> = emptyList(),
    val recentSearches: List<String> = listOf(
        "swords of justice",
        "adale maplesory",
        "nhạc nền - AnBeee 🐝",
        "失刃 | 花童",
        "mike who cheese harry",
        "Cry For Me"
    ),
    val youMayLike: List<String> = listOf(
        "lol esports live",
        "elden ring",
        "Nhạc tiktok hay",
        "Man City Vs Man United",
        "Huyền cơ pve"
    ),
    val navigateToResult: Boolean = false
)