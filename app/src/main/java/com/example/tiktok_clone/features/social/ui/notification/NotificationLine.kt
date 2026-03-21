package com.example.tiktok_clone.features.social.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tiktok_clone.features.social.data.model.FollowNotification
import com.example.tiktok_clone.features.social.data.model.FollowNotificationReceiptStatus
import com.example.tiktok_clone.features.social.data.model.Notification
import com.example.tiktok_clone.features.social.data.model.NotificationActionType
import com.example.tiktok_clone.features.social.data.model.NotificationReceiptStatus
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.toDateString

@Composable
fun NotificationLine(
    socialNotification: Notification? = null,
    followNotification: FollowNotification? = null,
    notificationType: String = "social",
    fromUserName: String,
    fromAvatarUrl: String,
    postMediaUrl: String? = null,
    postType: String = "ảnh",
) {
    val isNew = if (notificationType == "social") {
        socialNotification?.receiptStatus == NotificationReceiptStatus.DELIVERED
    } else {
        followNotification?.receiptStatus == FollowNotificationReceiptStatus.DELIVERED
    }
    val notificationActionType = when (socialNotification?.actionType) {
        NotificationActionType.LIKE -> "đã thích $postType của bạn. "
        NotificationActionType.COMMENT -> "đã bình luận về $postType của bạn. "
        NotificationActionType.COMMENT_REPLY -> "đã trả lời bình luận của bạn. "
        NotificationActionType.COMMENT_LIKE -> "đã thích bình luận của bạn. "
        NotificationActionType.REPOST -> "đã đăng lại cùng một bài đăng. "
        null -> ""
    }
    val createdAtMillis =
        if (notificationType == "social") socialNotification?.createdAt else followNotification?.createdAt
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isNew) Color(0xFFEAF4FF) else Color.Transparent)
            .padding(vertical = 6.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            avatarUrl = fromAvatarUrl,
            avatarSize = 50,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(fromUserName)
                    append(" ")
                }
                withStyle(
                    SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                ) {
                    append(if (notificationType == "social") notificationActionType else "đã theo dõi bạn. ")
                }
                withStyle(
                    SpanStyle(
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(createdAtMillis?.toDateString() ?: "")
                }
            },
            maxLines = 2,
            lineHeight = 16.sp,
            modifier = Modifier.weight(1f)
        )
        if (notificationType == "social") {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = postMediaUrl,
                    contentDescription = "avatar",
                    modifier = Modifier
                        .matchParentSize()
                )
            }
        }
    }
}