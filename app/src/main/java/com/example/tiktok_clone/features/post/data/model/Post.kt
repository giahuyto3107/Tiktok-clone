package com.example.tiktok_clone.features.post.data.model

enum class PostType {
    VIDEO, IMAGE
}

data class Post(
    val id: Int = 0,
    val userId: String = "0",
    val type: PostType = PostType.VIDEO, //enum
    val mediaUrl: String = "",
    val thumbnailUrl: String = "",       // Nếu là video thì có thumb, nếu là ảnh thì dùng chung mediaUrl
    val caption: String = "",
    val musicName: String? = null,
    val likeCount: Long = 0,
    val commentCount: Long = 0,
    val saveCount: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
