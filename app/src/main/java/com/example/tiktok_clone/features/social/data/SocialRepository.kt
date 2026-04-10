package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.Comment
import retrofit2.Response

/**
 * Repository mỏng wrap các call tới SocialApiService.
 *
 * Tách riêng cho dễ test và sau này mở rộng cache/local DB nếu cần.
 */
class SocialRepository(
    private val apiService: SocialApiService,
) {

    private fun ensureSuccess(response: Response<*>, action: String) {
        if (!response.isSuccessful) {
            throw IllegalStateException("$action failed with HTTP ${response.code()}")
        }
    }

    // region Follow

    suspend fun followUser(targetUid: String) {
        ensureSuccess(apiService.followUser(targetUid), "followUser")
    }

    suspend fun unfollowUser(targetUid: String) {
        ensureSuccess(apiService.unfollowUser(targetUid), "unfollowUser")
    }

    //    suspend fun getFollowers(uid: String) =
//        apiService.getFollowers(uid).users.map { it.profile }
    suspend fun getFollowCounts(uid: String): FollowCountResponse = apiService.getFollowCounts(uid)
    suspend fun getFollowers(uid: String) = apiService.getFollowers(uid).users.map { it.profile }
    suspend fun getFollowing(uid: String) = apiService.getFollowing(uid).users.map { it.profile }

    suspend fun getComments(
        postId: String,
        limit: Int? = null,
        offset: Int? = null,
    ): List<Comment> = apiService.getComments(
        postId = postId,
        limit = limit,
        offset = offset,
    )

    suspend fun getPostState(postId: String): PostStateResponse = apiService.getPostState(postId)


    suspend fun addComment(
        postId: String,
        content: String,
        parentId: String? = null,
        imageUri: String? = null,
    ): Comment {
        return apiService.createComment(
            postId = postId,
            body = CreateCommentRequest(
                content = content,
                parentId = parentId,
                imageUri = imageUri,
            ),
        )
    }

    suspend fun likePost(postId: String) {
        ensureSuccess(apiService.likePost(postId), "likePost")
    }

    suspend fun unlikePost(postId: String) {
        ensureSuccess(apiService.unlikePost(postId), "unlikePost")
    }

    suspend fun likeComment(commentId: String) {
        ensureSuccess(apiService.likeComment(commentId), "likeComment")
    }

    suspend fun unlikeComment(commentId: String) {
        ensureSuccess(apiService.unlikeComment(commentId), "unlikeComment")
    }

    suspend fun savePost(postId: String) {
        ensureSuccess(apiService.savePost(postId), "savePost")
    }

    suspend fun unSavePost(postId: String) {
        ensureSuccess(apiService.unSavePost(postId), "unSavePost")
    }

    suspend fun sharePost(postId: String) {
        ensureSuccess(apiService.sharePost(postId), "sharePost")
    }

    suspend fun unSharePost(postId: String) {
        ensureSuccess(apiService.unSharePost(postId), "unSharePost")
    }

    // endregion
}

