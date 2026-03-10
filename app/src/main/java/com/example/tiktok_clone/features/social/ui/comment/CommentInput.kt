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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.model.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.ui.theme.RedHeart
import com.example.tiktok_clone.ui.theme.TextPrimaryGray
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowUp

@Composable
fun CommentInput(
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = viewModel(),
    post: Post,
    user: User,
    isCommenting: Boolean,
    onCommenting: () -> Unit,
    onDismiss: () -> Unit
) {

    var commentText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .then(modifier)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(
                modifier = Modifier
                    .border(0.1.dp, TextPrimaryGray.copy(alpha = 0.5f), CircleShape)
                    .size(50.dp)
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
                    .heightIn(min = 50.dp)

            ) {
                TextField(
                    value = commentText,
                    onValueChange = {
                        commentText = it
                        onCommenting()
                    },
                    textStyle = TextStyle(color = Color.Black),
                    placeholder = {
                        Text("Thêm bình luận...", color = Color.Gray)

                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .onFocusChanged(
                            onFocusChanged = {
                                if (!it.isFocused) {
                                    onDismiss()
                                }
                            }
                        )
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
                            viewModel.onAction(SocialAction.AddComment(post.id.toString(), commentText, user))
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
