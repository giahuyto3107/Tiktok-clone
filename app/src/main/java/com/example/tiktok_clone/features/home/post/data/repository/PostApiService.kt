package com.example.tiktok_clone.features.home.post.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostApiService {
    @Multipart
    @POST("api/v1/posts/upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("user_id") userId: RequestBody
    ): Response<Unit>

    @Multipart
    @POST("api/v1/posts/upload/video")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("user_id") userId: RequestBody
    ): Response<Unit>
}