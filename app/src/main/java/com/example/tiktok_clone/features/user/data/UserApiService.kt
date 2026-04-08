package com.example.tiktok_clone.features.user.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Response from GET /api/v1/users
 */
data class FirebaseUserResponse(
    val uid: String,
    val email: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("email_verified") val emailVerified: Boolean = false,
    val disabled: Boolean = false,
    val providers: List<String> = emptyList(),
    @SerializedName("creation_timestamp") val creationTimestamp: Long? = null,
    @SerializedName("last_sign_in_timestamp") val lastSignInTimestamp: Long? = null,
)

data class FirebaseUserListResponse(
    val users: List<FirebaseUserResponse>,
    val total: Int,
)

interface UserApiService {
    @GET("api/v1/users")
    suspend fun getUsers(): FirebaseUserListResponse

    @GET("api/v1/users/{uid}")
    suspend fun getUser(@Path("uid") uid: String): FirebaseUserResponse

    @retrofit2.http.Multipart
    @retrofit2.http.POST("api/v1/users/upload_avatar")
    suspend fun uploadAvatar(
        @retrofit2.http.Part file: okhttp3.MultipartBody.Part
    ): UploadAvatarResponse
}

data class UploadAvatarResponse(
    val status: String,
    val avatarUrl: String
)
