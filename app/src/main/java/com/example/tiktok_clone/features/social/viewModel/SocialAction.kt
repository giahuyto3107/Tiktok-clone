package com.example.tiktok_clone.features.social.viewModel;

sealed interface SocialAction{
    data class Like(val postId: String): SocialAction
    data class Comment(val postId: String): SocialAction
    data class Share(val postId: String): SocialAction
    data class Save(val postId: String): SocialAction
    data class Follow(val userId: String): SocialAction

    data class DismissCommentSheet(val postId: String): SocialAction
    data class DismissShareSheet(val postId: String): SocialAction

    data object Refresh: SocialAction
    data object LoadMore: SocialAction
    data object Retry: SocialAction
    data object DismissError: SocialAction

}
