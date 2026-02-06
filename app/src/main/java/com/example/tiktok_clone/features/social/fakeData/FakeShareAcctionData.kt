package com.example.tiktok_clone.features.social.fakeData
import com.example.tiktok_clone.features.social.model.ShareCategory
import com.example.tiktok_clone.features.social.model.ShareItem


object FakeShareActionData {
    val shareItems = listOf(
        ShareItem(
            id = "repost",
            title = "Đăng lại",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "copy_link",
            title = "Sao Chép Liên Kết",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "zalo",
            title = "Zalo",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "facebook",
            title = "Facebook",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "facebook_lite",
            title = "Lite",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "sms",
            title = "SMS",
            category = ShareCategory.APP
        ),
        ShareItem(
            id = "report",
            title = "Báo Cáo",
            category = ShareCategory.REPORT
        ),
        ShareItem(
            id = "not_interested",
            title = "Không quan tâm",
            category = ShareCategory.REPORT
        ),
        ShareItem(
            id ="add_to_story",
            title ="Thêm vào nhật ký",
            category = ShareCategory.REPORT
        ),
        ShareItem(
            id = "speed",
            title = "Tốc độ phát lại",
            category = ShareCategory.REPORT
        ),
        ShareItem(
            id = "cast",
            title = "Chếu",
            category = ShareCategory.REPORT
        )
    )
}