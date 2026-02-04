package com.example.tiktok_clone.features.social.ui.share

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun ShareFriendItem(
    onShare: (User) -> Unit,
    friend: User,
    viewModel: SocialViewModel = viewModel(),
) {
    val selectedFriendShare by viewModel.selectedFriendShare.collectAsState()
    val isOnShare = friend.userID in selectedFriendShare

    Column(
        modifier = Modifier.clickable(onClick = {
            onShare(friend)
            viewModel.onAction(SocialAction.SelectedFriendShare(friend.userID))
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(0.2.dp, Color.LightGray, CircleShape)
                    .size(55.dp)
                    .clip(CircleShape)
            ) {
                Avatar(
                    avatarUrl = friend.avatarUrl,
                    modifier = Modifier
                        .matchParentSize()
                )
                if (isOnShare) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    )
                }
            }
            if (isOnShare) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Share",
                    tint = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier
                        .padding(end = 6.dp, bottom = 6.dp)
                        .size(20.dp)
                        .border(width = 0.2.dp, color = Color.White, shape = CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        Text(
            text = friend.userName,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(50.dp),
            maxLines = 2,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}