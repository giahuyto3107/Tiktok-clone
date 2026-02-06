package com.example.tiktok_clone.features.social.model

import android.provider.MediaStore
import androidx.annotation.DrawableRes

//old
//data class Post(
//    val id: String,
//    val userId: String,
//    val userName: String,
//    val description: String,
//    @DrawableRes val thumbnailRes: Int,
//
//    val isLiked: Boolean = false,
//    val likeCount: Int,
//
//    val commentCount: Int,
//
//    val isSaved: Boolean = false,
//    val saveCount: Int,
//    val isShared: Boolean = false,
//    val shareCount: Int,
//
//    val isFollowing: Boolean = false,
//
//    ) {
//
//}
//new
data class Post(
    val id: String,
    val author: User,

    val videoUrl: String,
    val thumbnailUrl: String?,

    val description: String? = null,

    // stats
    val likeCount: Long,
    val commentCount: Long,
    val shareCount: Long,
    val saveCount: Long,

    // user state
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,

    val createdAt: Long
)
