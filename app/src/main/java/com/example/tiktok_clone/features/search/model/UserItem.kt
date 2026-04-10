package com.example.tiktok_clone.features.search.model

import com.google.gson.annotations.SerializedName

data class UserItem(
    val id: Int = 0,
    @SerializedName("uid")
    val uid: String = "",
    @SerializedName("displayName")
    val displayName: String = "",
    val handle: String = "",
    val avatar: String = "",
    @SerializedName("followerCount")
    val followerCount: Int = 0,
    @SerializedName("totalLikes")
    val totalLikes: Int = 0,
    @SerializedName("isFollowed")
    val isFollowed: Boolean = false,
)
