package com.example.tiktok_clone.features.social.viewModel;

sealed interface SocialAction{
    data class Like(val postId: String): SocialAction
    data class LikeComment(val commentId: String): SocialAction
    data class Comment(val postId: String): SocialAction
    data class Share(val postId: String): SocialAction
    data class Save(val postId: String): SocialAction
    data class Follow(val userId: String): SocialAction

    data class SelectedFriendShare(val friendId: String): SocialAction

    data object ClearSelectedFriendShare: SocialAction
    data class DismissCommentSheet(val postId: String): SocialAction
    data class DismissShareSheet(val postId: String): SocialAction
    data class SelectReason(val reasonId: Int): SocialAction

    data class AddComment(val postId: String, val commentText: String): SocialAction
    data class SelectSpeed(val speedId: Int): SocialAction
    data class SelectNotInterested(val notInterestedId: Int): SocialAction
    data class SelectReport(val reportId: Int)
    data object Refresh: SocialAction
    data object LoadMore: SocialAction
    data object Retry: SocialAction
    data object DismissError: SocialAction

}
