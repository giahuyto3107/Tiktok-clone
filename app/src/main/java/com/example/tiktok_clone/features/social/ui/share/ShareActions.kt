package com.example.tiktok_clone.features.social.ui.share

import android.content.ClipData
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(UnstableApi::class)
@Composable
fun ShareActions(
    currentPost: Post,
    currentUser: User?,
    isShared: Boolean,
    socialViewModel: SocialViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        contentPadding = PaddingValues(start = 8.dp)

    ) {
        item {
            Box(
                modifier = Modifier
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                ShareActionItem(
                    icon = Icons.Filled.Repeat,
                    iconName = if (isShared) "Xóa đăng lại" else "Đăng lại",
                    iconSize = 40,
                    isText = false,
                    backgroundColor = Color(0xFFFEC20D),
                    modifier = Modifier
                        .rotate(90f),
                    onClick = {
                        socialViewModel.onAction(
                            SocialAction.Share(
                                currentPost.id.toString()
                            )
                        )
                    },
                )
                if (isShared)
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Check,
                        contentDescription = "Repeat",
                        tint = Color.White,
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 18.dp)
                    )
            }
        }
        item {
            ShareActionItem(
                icon = Icons.Outlined.Link,
                iconName = "Sao chép",
                iconSize = 35,
                isText = false,
                backgroundColor = Color(0xFF3175FA),
                onClick = {
                    scope.launch {
                        clipboardManager.setClipEntry(
                            ClipEntry(ClipData.newPlainText("link", currentPost.thumbnailUrl))
                        )
                        Toast.makeText(context, "Đã sao chép", Toast.LENGTH_SHORT).show()

                    }
                },
                modifier = Modifier
                    .rotate(135f),
            )
        }
        item {
            ShareActionItem(
                iconName = "Zalo",
                isText = true,
                textIcon = "Zalo",
                textIconSize = 14,
                textColor = Color.White,
                backgroundColor = Color(0xFF3175FA),
                onClick = {},
            )
        }
        item {
            ShareActionItem(
                iconName = "Facebook",
                isText = true,
                textIcon = "f",
                textIconSize = 50,
                textColor = Color.White,
                backgroundColor = Color(0xFF3175FA),
                onClick = {},
            )
        }
        item {
            ShareActionItem(
                icon = Icons.Filled.Mail,
                iconName = "Email",
                iconSize = 35,
                isText = false,
                backgroundColor = Color.Cyan,
                onClick = {},
            )
        }
        item {
            ShareActionItem(
                iconName = "Lite",
                isText = true,
                textIcon = "f",
                textIconSize = 50,
                textColor = Color(0xFF3175FA),
                backgroundColor = Color.White,
                onClick = {},
            )
        }

        item {
            ShareActionItem(
                icon = Icons.Filled.MoreHoriz,
                iconName = "Thêm",
                iconSize = 35,
                isText = false,
                backgroundColor = Color(0xFF3175FA),
                onClick = {},
            )
        }
    }
}