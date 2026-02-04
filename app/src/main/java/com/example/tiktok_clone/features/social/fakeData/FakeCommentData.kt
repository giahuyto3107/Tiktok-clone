package com.example.tiktok_clone.features.social.fakeData

import com.example.tiktok_clone.features.social.model.Comment

object FakeCommentData {
    val comments = listOf(
        Comment(
            id = "c1",
            postId = "p1",
            userId = "u1",
            userName = "Trùm Đẩy Deadline",
            createdAt = 1739145600000L, // 10-02-2025
            avatarUrl = "https://i.pravatar.cc/150?img=1",
            commentContent = "Video cuốn thật sự 👍",
            likeCount = 120,
            replyCount = 8
        ),
        Comment(
            id = "c2",
            postId = "p1",
            userId = "u2",
            userName = "Cha đẻ Tiktok",
            createdAt = 1762992000000L, // 10-02-2026
            avatarUrl = "https://i.pravatar.cc/150?img=2",
            commentContent = "Cười mà xem hoài",
            likeCount = 45,
            replyCount = 2
        ),
        Comment(
            id = "c3",
            postId = "p1",
            userId = "u3",
            userName = "Code Không Bug",
            createdAt = 1762732800000L, // 10-11-2025
            avatarUrl = "https://i.pravatar.cc/150?img=3",
            commentContent = "Đoạn cuối hơi nhanh, giá mà chậm lại chút",
            likeCount = 12,
            replyCount = 1
        ),
        Comment(
            id = "c4",
            postId = "p1",
            userId = "u4",
            userName = "Chúa Tể Ngủ Trễ",
            createdAt = 1765411200000L, // 10-12-2025
            avatarUrl = "https://i.pravatar.cc/150?img=4",
            commentContent = "Ủa rồi sao nữa 🤔",
            likeCount = 3,
            replyCount = 0
        ),
        Comment(
            id = "c5",
            postId = "p1",
            userId = "u5",
            userName = "Ông Hoàng Dark Mode",
            createdAt = 1736448000000L, // 08-01-2025
            avatarUrl = "https://i.pravatar.cc/150?img=5",
            commentContent = "Edit mượt ghê",
            isLiked = true,
            likeCount = 89,
            replyCount = 5
        ),
        Comment(
            id = "c6",
            postId = "p1",
            userId = "u6",
            userName = "Chuông kì bu",
            createdAt = 1739145600000L, // 10-02-2025
            avatarUrl = "",
            commentContent = "Xem giải trí ổn áp",
            likeCount = 0,
            replyCount = 0
        ),
        Comment(
            id = "c7",
            postId = "p1",
            userId = "u7",
            userName = "Tay Chơi Loli",
            createdAt = 1738886400000L, // 08-02-2025
            avatarUrl = "https://i.pravatar.cc/150?img=6",
            commentContent = "Xem mà tim đập nhanh hơn deadline 😭",
            likeCount = 666,
            replyCount = 69611
        ),
        Comment(
            id = "c8",
            postId = "p1",
            userId = "u8",
            userName = "Xe Lăng Thần Tốc",
            createdAt = 1738972800000L, // 09-02-2025
            avatarUrl = "https://i.pravatar.cc/150?img=7",
            commentContent = "Tua chưa kịp là hết, coi lại lần 3 luôn 🚀",
            isLiked = true,
            likeCount = 314,
            replyCount = 12
        ),
        Comment(
            id = "c9",
            postId = "p1",
            userId = "u9",
            userName = "Hoa Thanh Quế",
            createdAt = 1739232000000L, // 11-02-2025 (giả lập "2 giờ trước")
            avatarUrl = "https://i.pravatar.cc/150?img=8",
            commentContent = "Nói rằng mình là người Thanh H..",
            likeCount = 101,
            replyCount = 4
        )
    )
}
