package com.example.tiktok_clone.features.post.data.model

data class PostResponse(
    val posts: List<Post>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
