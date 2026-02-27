package com.example.tiktok_clone.features.social.model

sealed interface SocialAction{
    data class Like(val postId: String): SocialAction
    data class LikeComment(val commentId: String): SocialAction
    data class Comment(val postId: String): SocialAction
    data class Share(val postId: String): SocialAction
    data class Save(val postId: String): SocialAction
    data class Follow(val userId: String, val authorId: String): SocialAction

    data class SelectedFriendShare(val friendId: String): SocialAction

    data object ClearSelectedFriendShare: SocialAction


    data class AddComment(val postId: String, val commentText: String, val author: User): SocialAction
    data object DismissReportSheet: SocialAction
    data object OpenReportOption: SocialAction

}