package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowUp

@Composable
fun CommentInput(
    socialViewModel: SocialViewModel = koinViewModel(),
    post: Post,
    currentUser: User?,
    isCommenting: Boolean,
    onCommenting: () -> Unit,
    onDismiss: () -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.White)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Avatar(
                avatarUrl = currentUser?.avatarUrl,
                avatarSize = 40,
            )
            Box(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(horizontal = 10.dp)
                    .heightIn(min = 40.dp),
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = {
                        commentText = it
                        onCommenting()
                    },
                    textStyle = TextStyle(color = Color.Black),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (commentText.isEmpty()) {
                                Text(
                                    text = "Thêm bình luận...",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    },
                    minLines = 1,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .heightIn(45.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.LightGray.copy(0.5f))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .onFocusChanged(
                            onFocusChanged = {
                                if (!it.isFocused) {
                                    onDismiss()
                                }
                            }
                        ),
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
                    if (commentText.isEmpty()) RedHeart.copy(alpha = 0.6f) else RedHeart

                Button(
                    onClick = {
                        socialViewModel.onAction(
                            SocialAction.AddComment(
                                post.id.toString(),
                                commentText,
                                currentUser?.id.toString()
                            )
                        )
                        commentText = ""
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pushColor,
                        contentColor = Color.White
                    ),

                    ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.ArrowUp,
                        contentDescription = "Đăng",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
