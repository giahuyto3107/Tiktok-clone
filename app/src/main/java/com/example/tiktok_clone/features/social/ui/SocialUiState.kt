package com.example.tiktok_clone.features.social.ui

import com.example.tiktok_clone.features.social.model.Comment
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.model.Post

data class SocialUiState(
    val posts: List<Post> = emptyList(),
    val currentPostIndex: Int = 0,

    val comments: List<Comment> = emptyList(),
    val user: User? =  null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val error: String? = null,

    val showCommentSheet: Boolean = false,
    val showShareSheet: Boolean = false,
    val selectedPostId: String? = null,
    ){
    val currentPost: Post?
        get() = posts.getOrNull(currentPostIndex)

    val hasPosts: Boolean
        get() = posts.isNotEmpty()

    val canLoadMore: Boolean
        get() = !isLoading && !isRefreshing
}
