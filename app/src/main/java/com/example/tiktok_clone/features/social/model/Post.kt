package com.example.tiktok_clone.features.social.model

import android.provider.MediaStore
import androidx.annotation.DrawableRes

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val description: String,
    @DrawableRes val thumbnailRes: Int,


    val isLiked: Boolean = false,
    val likeCount: Int,

    val commentCount: Int,

    val isSaved: Boolean = false,
    val saveCount: Int,
    val isShared: Boolean = false,
    val shareCount: Int,

    val isFollowing: Boolean = false,

    ) {

}
fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "%.1fM".format(count / 1_000_000.0)
        count >= 1_000 -> "%.1k".format(count / 1_000)
        else -> count.toString()

    }
}

//CommentLine(
//AvatarUrl = "https://yt3.ggp",
//userName = "Nam đẹp trai",
//time = "2 giờ",
//comment = "Nam đẹp trai",
//modifier = Modifier.fillMaxWidth()
//)