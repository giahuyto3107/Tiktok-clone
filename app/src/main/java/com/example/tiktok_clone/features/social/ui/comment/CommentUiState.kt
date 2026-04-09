package com.example.tiktok_clone.features.social.ui.comment

import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.ui.CommentFeedUiState

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data class Error(val message: String) : CommentUiState
    data class Success(
        val comments: List<Comment> = emptyList(),
        val uploadState: UploadState = UploadState.Idle,
    ) : CommentUiState
}

// Convert comment feed uiState sang CommentUiState theo postId
fun CommentFeedUiState.toCommentUiState(
    postId: String,
    uploadState: UploadState,
    error: String?,
): CommentUiState {
    if (error != null) return CommentUiState.Error(error)
    val currentComments = commentsOf(postId)
    if (isCommentLoading(postId) && currentComments.isEmpty()) {
        return CommentUiState.Loading
    }
    return CommentUiState.Success(
        comments = currentComments,
        uploadState = uploadState,
    )
}
