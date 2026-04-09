package com.example.tiktok_clone.features.inbox.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.ui.theme.TikTokRed
import com.google.firebase.auth.FirebaseAuth

@Composable
// Nut send + avatar o bottom chat
fun MessageBottomInput(
    modifier: Modifier = Modifier,
    isMessage: Boolean = false,
    onSendClick: () -> Unit = {},
    socialViewModel: SocialViewModel = koinViewModel(),
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val currentUser = socialViewModel.getUser(currentUserId.toString())

    Row(
        modifier = Modifier
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            avatarUrl = currentUser.avatarUrl,
            avatarSize = 40,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(horizontal = if (isMessage) 4.dp else 12.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isMessage) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onSendClick
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        modifier = Modifier
                            .size(26.dp)
                            .rotate(-45f)
                            .scale(0.7f, 1f)
                            .clip(RoundedCornerShape(30.dp))
                            .offset(x = 3.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}