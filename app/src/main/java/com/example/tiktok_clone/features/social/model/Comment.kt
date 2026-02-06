package com.example.tiktok_clone.features.social.model

//data class Comment(
//    val id: String,
//    val postId: String,
//    val userId: String,
//    val userName: String = "",
//    val avatarUrl: String = "",
//    val createdAt: Long,
//    val commentContent: String,
//    val isLiked: Boolean = false,
//    val likeCount: Int = 0,
//    val isReply: Boolean = false,
//    val replyCount: Int = 0,
//)
data class Comment(
    val id: String,
    val postId: String,
    val author: User,

    val content: String,

    val likeCount: Long = 0,
    val isLiked: Boolean = false,

    val parentId: String? = null, // null = comment gốc
    val replyCount: Long = 0,

    val createdAt: Long
)
