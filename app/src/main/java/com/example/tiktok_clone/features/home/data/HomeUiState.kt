package com.example.tiktok_clone.features.home.data

import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.model.User

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
