package com.example.tiktok_clone.features.social.ui

import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.model.App
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.data.model.ShareItem

data class SocialUiState(
    val posts: List<Post> = emptyList(),
    val currentPostIndex: Int = 0,

    val comments: List<Comment> = emptyList(),
    val user: User? =  null,
    val friends: List<User> = emptyList(),

    val apps: List<App> = emptyList(),

    val shareActions: List<ShareItem> = emptyList(),


    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val error: String? = null,

    val showCommentSheet: Boolean = false,
    val showShareSheet: Boolean = false,
    val selectedPostId: String? = null,
    val uploadState: UploadState = UploadState.Idle,
    ){
    val currentPost: Post?
        get() = posts.getOrNull(currentPostIndex)

    val hasPosts: Boolean
        get() = posts.isNotEmpty()

    val canLoadMore: Boolean
        get() = !isLoading && !isRefreshing
}
