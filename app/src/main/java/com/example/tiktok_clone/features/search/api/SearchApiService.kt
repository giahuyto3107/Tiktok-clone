package com.example.tiktok_clone.features.search.api

import com.example.tiktok_clone.features.search.model.DiscoverResponse
import com.example.tiktok_clone.features.search.model.ProductItem
import com.example.tiktok_clone.features.search.model.SearchResponse
import com.example.tiktok_clone.features.search.model.UserItem
import com.example.tiktok_clone.features.search.model.VideoResult
import retrofit2.http.GET
import retrofit2.http.Query

/** Đường dẫn khớp backend `tiktokclone` (FastAPI root: `/search`, không có `api/v1`). */
interface SearchApiService {

    /** Gợi ý trang chủ: hot + preview (TikTok "Bạn có thể thích"). */
    @GET("search/discover")
    suspend fun discover(): DiscoverResponse

    @GET("search")
    suspend fun searchAll(
        @Query("q") query: String,
    ): SearchResponse

    @GET("search/suggest")
    suspend fun suggest(
        @Query("q") query: String,
    ): List<String>

    @GET("search/videos")
    suspend fun searchVideos(
        @Query("q") query: String,
    ): List<VideoResult>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
    ): List<UserItem>

    @GET("search/products")
    suspend fun searchProducts(
        @Query("q") query: String,
    ): List<ProductItem>

    /** Video LIVE; `q` rỗng = tất cả LIVE (backend filter theo từ khóa nếu có). */
    @GET("search/live")
    suspend fun searchLive(
        @Query("q") query: String? = null,
    ): List<VideoResult>
}
