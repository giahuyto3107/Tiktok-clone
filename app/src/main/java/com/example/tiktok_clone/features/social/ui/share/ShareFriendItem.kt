package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.ui.theme.TikTokRed

@Composable
// Render 1 friend item trong share picker
fun ShareFriendItem(
    onShare: (User) -> Unit,
    friend: User,
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    val sharePicker by socialViewModel.sharePickerUiState.collectAsState()
    val selectedFriendShare = sharePicker.selectedFriendShare
    val isOnShare = friend.id in selectedFriendShare

    Column(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
            onShare(friend)
            socialViewModel.onAction(
                SocialAction.SelectedFriendShare(friend.id)
            )
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val avatarSize = 50
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Avatar(
                avatarUrl = friend.avatarUrl,
                avatarSize = avatarSize,
            )
            if (isOnShare) {
                Box(
                    modifier = Modifier
                        .size(avatarSize.dp)
                        .background(Color.White.copy(alpha = 0.5f), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.White)
                            .size(20.dp)
                            .align(Alignment.BottomEnd)

                    )
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(color = TikTokRed)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        Text(
            text = friend.userName,
            fontSize = 10.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(65.dp),
            maxLines = 2,
            lineHeight = 12.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}