package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.Comment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

/**
 * Repository mỏng wrap các call tới SocialApiService.
 *
 * Tách riêng cho dễ test và sau này mở rộng cache/local DB nếu cần.
 */
class SocialRepository(
    private val apiService: SocialApiService,
) {

    // region Follow

    suspend fun followUser(targetUid: String) {
        apiService.followUser(targetUid)
    }

    suspend fun unfollowUser(targetUid: String) {
        apiService.unfollowUser(targetUid)
    }

    //    suspend fun getFollowers(uid: String) =
//        apiService.getFollowers(uid).users.map { it.profile }
    suspend fun getFollowers(uid: String) = apiService.getFollowers(uid).users.map { it.profile }
    suspend fun getFollowing(uid: String) = apiService.getFollowing(uid).users.map { it.profile }
    suspend fun getComments(postId: String): List<Comment> = apiService.getComments(postId)

    suspend fun getPostState(postId: String): PostStateResponse = apiService.getPostState(postId)


    suspend fun addComment(
        postId: String,
        content: String,
        parentId: String? = null,
    ): Comment {
        return apiService.createComment(
            postId = postId,
            body = CreateCommentRequest(
                content = content,
                parentId = parentId,
            ),
        )
    }

    suspend fun likePost(postId: String) {
        apiService.likePost(postId)
    }

    suspend fun unlikePost(postId: String) {
        apiService.unlikePost(postId)
    }

    suspend fun likeComment(commentId: String) {
        apiService.likeComment(commentId)
    }

    suspend fun unlikeComment(commentId: String) {
        apiService.unlikeComment(commentId)
    }

    suspend fun savePost(postId: String) {
        apiService.savePost(postId)
    }

    suspend fun unSavePost(postId: String) {
        apiService.unSavePost(postId)
    }

    suspend fun sharePost(postId: String) {
        apiService.sharePost(postId)
    }

    suspend fun unSharePost(postId: String) {
        apiService.unSharePost(postId)
    }

    // endregion
}

