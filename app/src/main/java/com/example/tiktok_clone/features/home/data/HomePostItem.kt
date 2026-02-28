package com.example.tiktok_clone.features.home.data

import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.model.SocialAction

data class HomePostItem(
    val post: Post,
//    val social: SocialAction,
    val isFollowing: Boolean = false
)
