import com.example.tiktok_clone.features.social.fakeData.FakeUserData.user
import com.example.tiktok_clone.features.social.model.Post

object FakePostData {
    val posts = listOf(
        Post(
            id = "p1",
            author = user[0],
            videoUrl = "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            thumbnailUrl = "https://picsum.photos/400/700?random=1",
            description = "Một ngày code Compose tới 2h sáng 💻☕",
            likeCount = 12_340,
            commentCount = 321,
            shareCount = 98,
            saveCount = 430,
            isLiked = true,
            isSaved = false,
            createdAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000
        ),
        Post(
            id = "p2",
            author = user[1],
            videoUrl = "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            thumbnailUrl = "https://picsum.photos/400/700?random=2",
            description = "UI đẹp là phải mượt 🖤✨",
            likeCount = 98_200,
            commentCount = 2_140,
            shareCount = 1_200,
            saveCount = 8_900,
            isLiked = false,
            isSaved = true,
            createdAt = System.currentTimeMillis() - 6 * 60 * 60 * 1000
        ),
        Post(
            id = "p3",
            author = user[2],
            videoUrl = "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            thumbnailUrl = "https://picsum.photos/400/700?random=3",
            description = "AI không đáng sợ, đáng sợ là không học AI 🤖🔥",
            likeCount = 1_200_000,
            commentCount = 45_000,
            shareCount = 32_000,
            saveCount = 120_000,
            isLiked = false,
            isSaved = false,
            createdAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000
        ),
        Post(
            id = "p4",
            author = user[3],
            videoUrl = "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            thumbnailUrl = "https://picsum.photos/400/700?random=4",
            description = "Design system không phải để cho đẹp 😌",
            likeCount = 45_600,
            commentCount = 876,
            shareCount = 210,
            saveCount = 1_900,
            isLiked = true,
            isSaved = true,
            createdAt = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000
        ),
        Post(
            id = "p5",
            author = user[4],
            videoUrl = "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            thumbnailUrl = "https://picsum.photos/400/700?random=5",
            description = "Cuộc sống chậm lại một chút 🌿",
            likeCount = 8_900,
            commentCount = 143,
            shareCount = 54,
            saveCount = 320,
            isLiked = false,
            isSaved = false,
            createdAt = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000
        )
    )
}