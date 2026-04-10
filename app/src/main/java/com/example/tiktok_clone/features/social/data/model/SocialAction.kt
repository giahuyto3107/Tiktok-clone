package com.example.tiktok_clone.features.social.data.model

sealed interface SocialAction {
    data class LikePost(val postId: String) : SocialAction
    data class LikeComment(val commentId: String) : SocialAction
    data class LoadComment(val postId: String) : SocialAction
    data class SharePost(val postId: String) : SocialAction
    data class SavePost(val postId: String) : SocialAction
    data class Follow(val authorId: String) : SocialAction

    data class SelectedFriendShare(val friendId: String) : SocialAction

    data object ClearSelectedFriendShare : SocialAction

    data class AddComment(
        val postId: String,
        val commentText: String,
        val userId: String,
        val parentId: String? = null,
    ) : SocialAction
}
