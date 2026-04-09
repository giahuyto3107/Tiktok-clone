package com.example.tiktok_clone.features.social.ui

import com.example.tiktok_clone.features.social.data.FollowUserResponse
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.User

data class PostFeedUiState(
    val postStates: Map<String, PostStateResponse> = emptyMap(),
)

data class CommentFeedUiState(
    val comments: List<Comment> = emptyList(),
    val commentLoading: Map<String, Boolean> = emptyMap(),
) {
    // Loc comments theo postId
    fun commentsOf(postId: String): List<Comment> = comments.filter { it.postId == postId }

    // Check trang thai loading comment theo postId
    fun isCommentLoading(postId: String): Boolean = commentLoading[postId] == true
}

data class SocialGraphUiState(
    val currentUser: User? = null,
    val friends: List<FollowUserResponse> = emptyList(),
    val followers: Set<String> = emptySet(),
    val following: Set<String> = emptySet(),
)

data class SharePickerUiState(
    val selectedFriendShare: Set<String> = emptySet(),
)
