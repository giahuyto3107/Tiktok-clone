package com.example.tiktok_clone.features.social.data

import com.example.tiktok_clone.features.social.data.model.Comment
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service mapping cho tất cả social endpoints.
 *
 * Lưu ý:
 * - Base URL được cấu hình trong ApiConfig.getBaseUrl() (AppModule).
 * - Ở đây giữ nguyên path như tài liệu: `/api/v1/...`
 */
interface SocialApiService {

    // region Follow / Follower

    @POST("api/v1/social/follow/{targetUid}")
    suspend fun followUser(
        @Path("targetUid") targetUid: String,
    ): Response<Unit>

    @DELETE("api/v1/social/follow/{targetUid}")
    suspend fun unfollowUser(
        @Path("targetUid") targetUid: String,
    ): Response<Unit>

    @GET("api/v1/social/{uid}/counts")
    suspend fun getFollowCounts(
        @Path("uid") uid: String,
    ): FollowCountResponse

    @GET("api/v1/social/{uid}/followers")
    suspend fun getFollowers(
        @Path("uid") uid: String,
    ): SocialUserListResponse

    @GET("api/v1/social/{uid}/following")
    suspend fun getFollowing(
        @Path("uid") uid: String,
    ): SocialUserListResponse

    // endregion

    // region Reaction (like / save / state)

    @POST("api/v1/social/posts/{postId}/like")
    suspend fun likePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @DELETE("api/v1/social/posts/{postId}/like")
    suspend fun unlikePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @POST("api/v1/social/posts/{postId}/save")
    suspend fun savePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @DELETE("api/v1/social/posts/{postId}/save")
    suspend fun unSavePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @GET("api/v1/social/posts/{postId}/state")
    suspend fun getPostState(
        @Path("postId") postId: String,
    ): PostStateResponse
    // endregion
    // region Share
    @POST("api/v1/social/posts/{postId}/repost")
    suspend fun sharePost(
        @Path("postId") postId: String,
    ): Response<Unit>

    @DELETE("api/v1/social/posts/{postId}/repost")
    suspend fun unSharePost(
        @Path("postId") postId: String,
    ): Response<Unit>
    // endregion

    // region Comment

    @GET("api/v1/social/posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): List<Comment>

    @POST("api/v1/social/posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: String,
        @Body body: CreateCommentRequest,
    ): Comment

    @POST("api/v1/social/comments/{commentId}/like")
    suspend fun likeComment(
        @Path("commentId") commentId: String,
    ): Response<Unit>

    @DELETE("api/v1/social/comments/{commentId}/like")
    suspend fun unlikeComment(
        @Path("commentId") commentId: String,
    ): Response<Unit>

    // SocialApiService.kt

    // endregion
}

// region DTOs

data class FollowCountResponse(
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
)



// Thêm DTOs mới
data class SocialUserListResponse(
    @SerializedName("users") val users: List<SocialUser>,
    @SerializedName("total") val total: Int,
)

data class SocialUser(
    @SerializedName("profile") val profile: FollowUserResponse,
    @SerializedName("isFollowing") val isFollowing: Boolean,
)

data class FollowUserResponse(
    @SerializedName("uid") val uid: String,
    @SerializedName("displayName") val username: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
)



data class PostStateResponse(
    @SerializedName("postId") val postId: String,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("shareCount") val shareCount: Int,
    @SerializedName("saveCount") val saveCount: Int,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("isSaved") val isSaved: Boolean,
    @SerializedName("isShared") val isShared: Boolean,
)

data class ShareResponse(
    @SerializedName("shareCount") val shareCount: Int,
    @SerializedName("isShared") val isShared: Boolean,
)

data class CreateCommentRequest(
    @SerializedName("content") val content: String,
    @SerializedName("parentId") val parentId: String? = null,
    @SerializedName("imageUri") val imageUri: String? = null,
)

// endregion

