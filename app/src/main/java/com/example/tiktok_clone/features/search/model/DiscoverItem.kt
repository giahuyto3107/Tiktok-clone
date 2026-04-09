package com.example.tiktok_clone.features.search.model

import com.google.gson.annotations.SerializedName

data class DiscoverResponse(
    val items: List<DiscoverItem> = emptyList(),
)

data class DiscoverItem(
    val keyword: String = "",
    val hot: Boolean = false,
    @SerializedName("previewThumb")
    val previewThumb: String? = null,
)
