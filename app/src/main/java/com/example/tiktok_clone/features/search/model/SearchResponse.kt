package com.example.tiktok_clone.features.search.model

/**
 * Backend `tiktokclone` chỉ trả `videos` + `users`. Gson không gán field thiếu → dùng nullable + orEmpty() khi map.
 */
data class SearchResponse(
    val videos: List<VideoResult>? = null,
    val users: List<UserItem>? = null,
    val products: List<ProductItem>? = null,
    val images: List<String>? = null,
    val lives: List<String>? = null,
)