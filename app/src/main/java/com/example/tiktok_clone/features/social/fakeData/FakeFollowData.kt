package com.example.tiktok_clone.features.social.fakeData

import com.example.tiktok_clone.features.social.model.Follow

object FakeFollowData {
    val follows = listOf(
        Follow("u1", "u2"),
        Follow("u2", "u1"), // → bạn
        Follow("u1", "u3"), // u3 không follow lại → không phải bạn
        Follow("u4", "u1"),  // fan
        Follow("u1", "u5"),
        Follow("u5", "u1"),
        Follow("u1", "u6"),
        Follow("u6", "u1"),
        Follow("u1", "u7"),
        Follow("u7", "u1"),
        Follow("u1", "u8"),
        Follow("u8", "u1"),
        Follow("u1", "u9"),
        Follow("u9", "u1"),
    )
}
