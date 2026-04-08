package com.example.tiktok_clone.features.search.model

data class SearchUiState(
    val query: String = "",
    val suggestions: List<String> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val discoverItems: List<DiscoverItem> = emptyList(),
    val isDiscoverLoading: Boolean = false,

    val videos: List<VideoResult> = emptyList(),
    val users: List<UserItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val images: List<String> = emptyList(),
    val lives: List<String> = emptyList(),

    val isLoading: Boolean = false,
    /** Đang gọi API /search/suggest sau debounce */
    val isSuggestLoading: Boolean = false,
    val error: String? = null,
    val navigateToResult: Boolean = false
)