package com.example.tiktok_clone.features.inbox.ui.inboxState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.BlueAccent
import com.example.tiktok_clone.ui.theme.Purple
import com.example.tiktok_clone.ui.theme.RedHeart
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heart

@Composable
fun InboxChatList(
    currentUser: User,
    viewModel: SocialViewModel = viewModel(),
    onChatClick: (userId: String) -> Unit = {}
) {
    val friends by viewModel.friends.collectAsState()
    viewModel.loadFriends(currentUser.id)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item{
            InboxNotiItem(
                icon = Icons.Filled.Group,
                iconSize = 36.dp,
                bgColor = BlueAccent,
                notiType = "Những Follower mới",
                notiContent = "Thêm bạn bè"
            )
        }
        item{
            InboxNotiItem(
                icon = FontAwesomeIcons.Solid.Heart,
                iconSize = 28.dp,
                bgColor = RedHeart,
                notiType = "Hoạt động",
                notiContent = "Thêm bạn bè"
            )
        }
        item{
            InboxNotiItem(
                icon = Icons.Filled.Notifications,
                iconSize = 36.dp,
                bgColor = Purple,
                notiType = "Thông báo hệ thống",
                notiContent = "Thêm bạn bè"
            )
        }
        items(friends.size) { index ->
            InboxChatItem(chatWith = friends[index], onChatClick = {onChatClick(friends[index].id)})
        }
    }
}