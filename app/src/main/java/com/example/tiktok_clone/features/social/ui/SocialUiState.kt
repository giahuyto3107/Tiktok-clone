package com.example.tiktok_clone.features.social.ui

import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.FollowCountResponse
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.User

sealed interface SocialUiState {
    object Loading : SocialUiState
    data class Success(val data: SocialUiData) : SocialUiState
    data class Error(val message: String) : SocialUiState
}

data class SocialUiData(
    val postStates: Map<String, PostStateResponse> = emptyMap(),
    val comments: List<Comment> = emptyList(),
    val commentHasMore: Map<String, Boolean> = emptyMap(),
    val currentUser: User? = null,
    val friends: List<FollowUserResponse> = emptyList(),
    val followers: Set<String> = emptySet(),
    val following: Set<String> = emptySet(),
    val followCounts: FollowCountResponse? = null,
    val selectedFriendShare: Set<String> = emptySet(),
    val uploadState: UploadState = UploadState.Idle,
    val showReportSheet: Boolean = false,
    val selectedPostId: String? = null,
) {
    fun commentsOf(postId: String): List<Comment> = comments.filter { it.postId == postId }

    fun canLoadMoreComments(postId: String): Boolean =
        (commentHasMore[postId] ?: true) && uploadState !is UploadState.Loading
}