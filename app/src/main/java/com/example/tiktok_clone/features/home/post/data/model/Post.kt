package com.example.tiktok_clone.features.home.post.data.model

enum class PostType {
    VIDEO, IMAGE
}

data class Post(
    val id: Long = 0,
    val authorId: Long = 0,
    val type: PostType = PostType.VIDEO,
    val mediaUrl: String = "",
    val thumbnailUrl: String = "",       // Nếu là video thì có thumb, nếu là ảnh thì dùng chung mediaUrl
    val description: String = "",
    val musicName: String = "Original Sound",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)