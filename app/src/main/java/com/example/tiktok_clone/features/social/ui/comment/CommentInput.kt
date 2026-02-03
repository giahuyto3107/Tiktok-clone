package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowUp

@Composable
fun CommentInput(
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier
) {

    var commentText by remember { mutableStateOf("") }
    var user by remember { mutableStateOf(viewModel.user.value) }
    var isCommenting by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Row(
            modifier = modifier
                .background(color = Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, CircleShape)
                    .size(45.dp)
                    .clip(CircleShape)


            ) {
                Avatar(
                    avatarUrl = user.avatarUrl,
                    modifier = Modifier
                        .matchParentSize()
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(color = Color.White)
                    .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                    .height(50.dp)

            ) {
                TextField(
                    value = commentText,
                    onValueChange = {
                        commentText = it
                        isCommenting = true
                    },
                    textStyle = TextStyle(color = Color.Black),
                    placeholder = {
                        Text("Thêm bình luận...", color = Color.Gray)

                    },
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.4f),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.4f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

            }

        }
        if (isCommenting) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // icon bên trái
                CommentInputItem()

                Spacer(modifier = Modifier.weight(1f))

                val pushColor =
                    if (!commentText.isEmpty()) Color.Red.copy(alpha = 0.6f) else Color.Red.copy(
                        alpha = 0.8f
                    )
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .height(30.dp)
                        .width(50.dp)
                        .clip(CircleShape)
                        .background(color = pushColor)
                        .clickable(onClick = {}),
                    contentAlignment = Alignment.Center
                ) {
                    CommentItem(
                        icon = FontAwesomeIcons.Solid.ArrowUp,
                        tint = Color.White,
                        text = "Đăng",
                        showText = false,
                        onClick = {},
                        modifier = Modifier.size(12.dp)

                    )
                }
            }
        }
    }
}
