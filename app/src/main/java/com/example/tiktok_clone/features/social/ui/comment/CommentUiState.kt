package com.example.tiktok_clone.features.social.ui.comment

import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.ui.SocialUiState

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data class Error(val message: String) : CommentUiState
    data class Success(
        val comments: List<Comment> = emptyList(),
        val hasMore: Boolean = true,
        val uploadState: UploadState = UploadState.Idle,
    ) : CommentUiState
}

fun SocialUiState.toCommentUiState(postId: String): CommentUiState = when (this) {
    is SocialUiState.Loading -> CommentUiState.Loading
    is SocialUiState.Error -> CommentUiState.Error(message)
    is SocialUiState.Success -> {
        val currentComments = data.commentsOf(postId)
        if (data.isCommentLoading(postId) && currentComments.isEmpty()) {
            CommentUiState.Loading
        } else {
            CommentUiState.Success(
                comments = currentComments,
                hasMore = data.canLoadMoreComments(postId),
                uploadState = data.uploadState,
            )
        }
    }
}
