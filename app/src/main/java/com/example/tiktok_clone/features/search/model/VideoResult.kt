package com.example.tiktok_clone.features.search.model


data class VideoResult(
    val thumbnail: Int,   // R.drawable.xxx
    val title: String,
    val author: String,
    val likes: Int
)