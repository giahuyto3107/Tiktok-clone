package com.example.tiktok_clone.features.social.fakeData

import com.example.tiktok_clone.features.social.fakeData.FakeUserData.user
import com.example.tiktok_clone.features.social.model.Comment

object FakeCommentData {
    val comments = listOf(
        Comment(
            id = "c1",
            postId = "p1",
            author = user[1],
            content = "Video này cuốn ghê 😭",
            likeCount = 120,
            isLiked = true,
            replyCount = 2,
            createdAt = System.currentTimeMillis() - 60_000
        ),
        Comment(
            id = "c2",
            postId = "p1",
            author = user[2],
            content = "Xem mà quên luôn thời gian",
            likeCount = 89,
            createdAt = System.currentTimeMillis() - 120_000
        ),
        Comment(
            id = "c3",
            postId = "p1",
            author = user[3],
            content = "Ai coi tới cuối giống mình không?",
            likeCount = 42,
            replyCount = 1,
            createdAt = System.currentTimeMillis() - 180_000
        ),
        Comment(
            id = "c4",
            postId = "p1",
            author = user[4],
            content = "Nhạc nền tên gì vậy mn?",
            likeCount = 64,
            createdAt = System.currentTimeMillis() - 240_000
        ),
        Comment(
            id = "c5",
            postId = "p1",
            author = user[5],
            content = "Xem mà nổi da gà luôn á",
            likeCount = 210,
            isLiked = true,
            createdAt = System.currentTimeMillis() - 300_000
        ),

        // ===== Replies =====
        Comment(
            id = "c6",
            postId = "p2",
            author = user[0],
            content = "Khúc cuối mới đã 😌",
            parentId = "c1",
            likeCount = 18,
            createdAt = System.currentTimeMillis() - 50_000
        ),
        Comment(
            id = "c7",
            postId = "p3",
            author = user[6],
            content = "Chuẩn luôn, coi lại mấy lần",
            parentId = "c1",
            likeCount = 9,
            createdAt = System.currentTimeMillis() - 40_000
        ),
        Comment(
            id = "c8",
            postId = "p4",
            author = user[7],
            content = "Tới cuối mới thấy hay",
            parentId = "c3",
            likeCount = 6,
            createdAt = System.currentTimeMillis() - 100_000
        ),

        // ===== More root comments =====
        Comment(
            id = "c9",
            postId = "p1",
            author = user[8],
            content = "Sao video này ít view vậy ta?",
            likeCount = 12,
            createdAt = System.currentTimeMillis() - 360_000
        ),
        Comment(
            id = "c10",
            postId = "p1",
            author = user[9],
            content = "Coi ban đêm là cuốn lắm",
            likeCount = 77,
            createdAt = System.currentTimeMillis() - 420_000
        ),
        Comment(
            id = "c11",
            postId = "p1",
            author = user[1],
            content = "Ai coi mà không tua không?",
            likeCount = 33,
            createdAt = System.currentTimeMillis() - 480_000
        ),
        Comment(
            id = "c12",
            postId = "p1",
            author = user[2],
            content = "Nội dung đơn giản mà hay",
            likeCount = 55,
            isLiked = true,
            createdAt = System.currentTimeMillis() - 540_000
        ),
        Comment(
            id = "c13",
            postId = "p1",
            author = user[3],
            content = "Xem mà thấy quen quen 🤔",
            likeCount = 21,
            createdAt = System.currentTimeMillis() - 600_000
        ),
        Comment(
            id = "c14",
            postId = "p1",
            author = user[4],
            content = "Ủa sao giống câu chuyện của mình ghê",
            likeCount = 98,
            replyCount = 1,
            createdAt = System.currentTimeMillis() - 660_000
        ),

        // ===== Reply nữa =====
        Comment(
            id = "c15",
            postId = "p1",
            author = user[5],
            content = "T cũng thấy vậy luôn",
            parentId = "c14",
            likeCount = 14,
            createdAt = System.currentTimeMillis() - 620_000
        ),

        // ===== Final batch =====
        Comment(
            id = "c16",
            postId = "p1",
            author = user[6],
            content = "Video kiểu coi hoài không chán",
            likeCount = 150,
            createdAt = System.currentTimeMillis() - 720_000
        ),
        Comment(
            id = "c17",
            postId = "p1",
            author = user[7],
            content = "Coi xong là muốn coi nữa",
            likeCount = 61,
            createdAt = System.currentTimeMillis() - 780_000
        ),
        Comment(
            id = "c18",
            postId = "p1",
            author = user[8],
            content = "Ai đang coi lúc 2h sáng giống mình không 😭",
            likeCount = 200,
            isLiked = true,
            createdAt = System.currentTimeMillis() - 840_000
        ),
        Comment(
            id = "c19",
            postId = "p1",
            author = user[9],
            content = "Coi mà quên cả deadline",
            likeCount = 44,
            createdAt = System.currentTimeMillis() - 900_000
        ),
        Comment(
            id = "c20",
            postId = "p1",
            author = user[0],
            content = "Ai thích kiểu video này giống mình không?",
            likeCount = 88,
            createdAt = System.currentTimeMillis() - 960_000
        )
    )
}
