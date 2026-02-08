package com.example.tiktok_clone.features.auth.model


data class ShopResult(
    val id: String,
    val name: String,
    val avatar: Int,      // ảnh shop
    val followers: Int,   // số người theo dõi
    val rating: Float     // đánh giá (vd: 4.8)
)