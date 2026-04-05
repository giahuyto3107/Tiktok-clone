package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.tiktok_clone.features.search.model.UserItem

@Composable
fun UserResultTab(
    users: List<UserItem>,
) {
    if (users.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có người dùng", color = Color.Gray)
        }
        return
    }

    val followOverride = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn {
        items(users, key = { it.id }) { user ->
            val followed = followOverride[user.id] ?: user.isFollowed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {}
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubcomposeAsyncImage(
                    model = user.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .border(2.dp, Color(0xFFFF2E63), CircleShape)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop,
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user.displayName.ifBlank { user.handle },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                    Text(
                        "@${user.handle}",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1,
                    )
                    Text(
                        "${formatCompactCount(user.followerCount)} follower · ${formatCompactCount(user.totalLikes)} lượt thích",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                }

                if (followed) {
                    Button(
                        onClick = { followOverride[user.id] = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = Color.Black,
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Text("Đã follow", fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = { followOverride[user.id] = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF2E63),
                            contentColor = Color.White,
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Text("Follow", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
