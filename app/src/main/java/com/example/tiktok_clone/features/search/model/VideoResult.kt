package com.example.tiktok_clone.features.search.model

import com.google.gson.annotations.SerializedName

data class VideoResult(
    val id: Int = 0,
    val thumbnail: String = "",
    val title: String = "",
    val author: String = "",
    val likes: Int = 0,
    @SerializedName("authorAvatar")
    val authorAvatar: String = "",
    @SerializedName("createdAt")
    val createdAt: Long? = null,
    @SerializedName("durationSeconds")
    val durationSeconds: Int = 0,
)
