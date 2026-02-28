package com.example.tiktok_clone.features.post.data.repository

import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.data.model.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PostApiService {
    @Multipart
    @POST("api/v1/posts/upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
        @Part("user_id") userId: RequestBody
    ): Response<Unit>

    @Multipart
    @POST("api/v1/posts/upload/video")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
        @Part("user_id") userId: RequestBody
    ): Response<Unit>

    @GET("api/v1/posts")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("type") type: String? = null,
        @Query("status") status: String? = null
    ): PostResponse
}