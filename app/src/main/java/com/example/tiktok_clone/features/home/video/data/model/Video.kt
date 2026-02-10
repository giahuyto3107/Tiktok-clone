package com.example.tiktok_clone.features.home.video.data.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Video(
    @DocumentId val id: String = "",
    val authorId: String = "",
    val authorName: String = "",     // Lưu thừa để đỡ query
    val authorAvatarUrl: String = "", // Lưu thừa
    val videoUrl: String = "",       // Link file mp4 trên Storage
    val thumbnailUrl: String = "",   // Link ảnh bìa
    val description: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Date = Date()
)