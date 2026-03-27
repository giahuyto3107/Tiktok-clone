package com.example.tiktok_clone.features.inbox.ui.components

import android.content.Context
import android.net.Uri
import com.example.tiktok_clone.features.inbox.data.ChatResponse
import com.example.tiktok_clone.features.inbox.data.MessageDto
import com.example.tiktok_clone.features.inbox.data.model.Message
import com.example.tiktok_clone.features.inbox.data.model.MessageStatus
import com.example.tiktok_clone.features.inbox.data.model.MessageType
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.example.tiktok_clone.features.social.data.model.Notification
import com.example.tiktok_clone.features.social.data.model.NotificationActionType
import com.example.tiktok_clone.features.social.data.model.User
import java.io.File

data class PickedMedia(
    val uri: Uri,
    val file: File,
    val type: String,
    val mimeType: String?,
)

val PlaceholderLastMessage = Message(
    id = "",
    content = "",
    senderId = "",
    timestamp = 0L,
    type = MessageType.TEXT,
    status = MessageStatus.SENT,
    imageUri = null,
)

fun Message.statusLabel(): String = when (receiptStatus) {
    MessageStatus.SEEN -> "Đã xem"
    MessageStatus.DELIVERED -> "Đã gửi"
    else -> when (status) {
        MessageStatus.SENDING -> "Đang gửi"
        else -> "Đã gửi"
    }
}

fun buildChatPreviewText(
    message: Message,
    chatWith: User,
    currentUserId: String?,
): String {
    val baseText = if (message.senderId == currentUserId) {
        message.statusLabel()
    } else if (message.receiptStatus == MessageStatus.SEEN) {
        "Đã xem"
    } else {
        message.content
    }

    return when (message.type) {
        MessageType.IMAGE -> "${chatWith.userName} đã gửi cho bạn một ảnh"
        MessageType.VIDEO -> "${chatWith.userName} đã gửi cho bạn một video"
        MessageType.TEXT -> baseText
    }
}

fun buildFollowNotificationText(
    follower: User?,
    latestFollowNotification: FollowNotification?,
): String {
    if (follower == null || latestFollowNotification == null) {
        return "Chưa có thông báo"
    }
    return "${follower.userName} đã theo dõi bạn"
}

fun buildSocialNotificationText(
    actor: User?,
    latestNotification: Notification?,
    postType: PostType?,
): String {
    if (actor == null || latestNotification == null) {
        return "Chưa có thông báo"
    }

    val postLabel = if (postType == PostType.VIDEO) "video" else "ảnh"
    val actionText = when (latestNotification.actionType) {
        NotificationActionType.LIKE -> "đã thích $postLabel của bạn"
        NotificationActionType.COMMENT -> "đã bình luận về $postLabel của bạn"
        NotificationActionType.COMMENT_REPLY -> "đã trả lời bình luận của bạn"
        NotificationActionType.COMMENT_LIKE -> "đã thích bình luận của bạn"
        NotificationActionType.REPOST -> "đã đăng lại cùng một bài đăng"
    }
    return "${actor.userName} $actionText"
}

fun resolveLastMessage(
    chat: ChatResponse,
    getLastMessage: (MessageDto?) -> Message?,
): Message = getLastMessage(chat.lastMessage) ?: PlaceholderLastMessage

fun resolvePickedMedia(
    context: Context,
    uri: Uri,
): PickedMedia? {
    val mimeType = context.contentResolver.getType(uri)
    val type = if (mimeType?.startsWith("video") == true) "VIDEO" else "IMAGE"
    val extension = when {
        mimeType?.startsWith("video") == true -> ".mp4"
        mimeType == "image/jpeg" || mimeType == "image/jpg" -> ".jpg"
        mimeType == "image/png" -> ".png"
        mimeType == "image/gif" -> ".gif"
        mimeType == "image/webp" -> ".webp"
        else -> ".jpg"
    }
    val file = uriToTempFile(context, uri, extension) ?: return null
    return PickedMedia(
        uri = uri,
        file = file,
        type = type,
        mimeType = mimeType,
    )
}

fun uriToTempFile(context: Context, uri: Uri, extension: String): File? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val file = File.createTempFile("inbox_msg_", extension, context.cacheDir)
        input.use { stream ->
            file.outputStream().use { out -> stream.copyTo(out) }
        }
        file
    } catch (_: Exception) {
        null
    }
}