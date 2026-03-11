package com.example.tiktok_clone.features.inbox.fakeData

import com.example.tiktok_clone.features.inbox.model.Message
import com.example.tiktok_clone.features.inbox.model.MessageStatus
import com.example.tiktok_clone.features.inbox.model.MessageType

object FakeMessData {
    const val ME = "u1"
    const val OTHER = "u2"

    val messages = listOf(
        Message(
            id = "1",
            content = "Hey! bạn có rảnh không?",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 7200_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "2",
            content = "mình cần hỏi chút về cái project",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 7199_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "3",
            content = "gấp lắm á 😅",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 7198_000,
            type = MessageType.TEXT,
        ),
        // Mình reply chậm
        Message(
            id = "4",
            content = "ừ có gì không",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 6000_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        // OTHER hỏi dồn
        Message(
            id = "5",
            content = "cái TikTok clone bạn đang làm",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 5900_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "6",
            content = "dùng CameraX à?",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 5899_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "7",
            content = "mình thấy trên github của bạn",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 5898_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "8",
            content = "https://picsum.photos/300/200",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 5897_000,
            type = MessageType.IMAGE,
            imageUri = "https://picsum.photos/300/200",
        ),
        Message(
            id = "9",
            content = "cái màn hình camera trông xịn vậy",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 5896_000,
            type = MessageType.TEXT,
        ),
        // Mình giải thích
        Message(
            id = "10",
            content = "ừ dùng CameraX + Jetpack Compose",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 5000_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        Message(
            id = "11",
            content = "kết hợp với LifecycleCameraController",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 4999_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        Message(
            id = "12",
            content = "handle cả record video lẫn chụp ảnh trong cùng 1 controller",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 4998_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        // OTHER hỏi tiếp
        Message(
            id = "13",
            content = "permission handling thế nào?",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 4000_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "14",
            content = "mình đang bị stuck ở chỗ đó",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 3999_000,
            type = MessageType.TEXT,
        ),
        // Mình gửi ảnh minh họa
        Message(
            id = "15",
            content = "",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 3500_000,
            type = MessageType.IMAGE,
            imageUri = "https://picsum.photos/seed/code/300/200",
            status = MessageStatus.SEEN,
        ),
        Message(
            id = "16",
            content = "dùng DisposableEffect + LifecycleEventObserver",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 3499_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        Message(
            id = "17",
            content = "ON_RESUME thì checkPermissions lại, tránh bị stuck sau khi user vào Settings rồi quay lại",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 3498_000,
            type = MessageType.TEXT,
            status = MessageStatus.SEEN,
        ),
        // OTHER cảm ơn
        Message(
            id = "18",
            content = "ồ hay quá",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 2000_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "19",
            content = "cảm ơn bạn nhiều nha",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 1999_000,
            type = MessageType.TEXT,
        ),
        Message(
            id = "20",
            content = "🙏🙏🙏",
            senderId = OTHER,
            timestamp = System.currentTimeMillis() - 1998_000,
            type = MessageType.TEXT,
        ),
        // Mình reply
        Message(
            id = "21",
            content = "không có gì, hmu nếu bị stuck tiếp nhé",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 1000_000,
            type = MessageType.TEXT,
            status = MessageStatus.DELIVERED,
        ),
        // Tin nhắn mới nhất đang gửi
        Message(
            id = "22",
            content = "mình gửi thêm cho cái video demo nè",
            senderId = ME,
            timestamp = System.currentTimeMillis() - 100,
            type = MessageType.VIDEO,
            imageUri = "https://picsum.photos/seed/video/300/200",
            status = MessageStatus.SENDING,
        ),
    )
}