package com.example.tiktok_clone.features.social.fakeData

import com.example.tiktok_clone.features.social.model.User

data object FakeUserData{
    val user = listOf(
        User(
            id = "u1",
            userName = "Nam đẹp trai",
            avatarUrl = "https://i.pravatar.cc/150?img=7",
            followerCount = 1280,
            followingCount = 312,
            isFollowing = false // chính mình
        ),
        User(
            id = "u2",
            userName = "Linh cute",
            avatarUrl = "https://i.pravatar.cc/150?img=9",
            followerCount = 980,
            followingCount = 201,
            isFollowing = true
        ),
        User(
            id = "u3",
            userName = "Huy trầm tính",
            avatarUrl = "https://i.pravatar.cc/150?img=13",
            followerCount = 432,
            followingCount = 180,
            isFollowing = true
        ),
        User(
            id = "u4",
            userName = "Mai hay cười",
            avatarUrl = null,
            followerCount = 2560,
            followingCount = 98,
            isFollowing = false
        ),
        User(
            id = "u5",
            userName = "An coder",
            avatarUrl = "https://i.pravatar.cc/150?img=15",
            followerCount = 340,
            followingCount = 410,
            isFollowing = true
        ),
        User(
            id = "u6",
            userName = "Thảo nghiện UI",
            avatarUrl = "https://i.pravatar.cc/150?img=16",
            followerCount = 1780,
            followingCount = 622,
            isFollowing = true
        ),
        User(
            id = "u7",
            userName = "Chấm hỏi to đùng",
            avatarUrl = "https://i.pravatar.cc/150?img=17",
            followerCount = 89,
            followingCount = 120,
            isFollowing = false
        ),
        User(
            id = "u8",
            userName = "zzbestnamzz",
            avatarUrl = "https://i.pravatar.cc/150?img=18",
            followerCount = 5600,
            followingCount = 12,
            isFollowing = true
        ),
        User(
            id = "u9",
            userName = "?????",
            avatarUrl = "https://i.pravatar.cc/150?img=19",
            followerCount = 12,
            followingCount = 300,
            isFollowing = false
        ),
        User(
            id = "u10",
            userName = "xe lăng thần tốc",
            avatarUrl = "https://i.pravatar.cc/150?img=46",
            followerCount = 820,
            followingCount = 77,
            isFollowing = true
        )
    )
}
