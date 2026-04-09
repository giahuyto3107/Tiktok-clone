package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowUp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import com.example.tiktok_clone.ui.theme.TikTokRed
import com.example.tiktok_clone.features.social.ui.components.EmotionRow

@Composable
// Input comment + submit
fun CommentInput(
    socialViewModel: SocialViewModel = koinViewModel(),
    postId: String,
    currentUser: User?,
    commentRoot: Comment?,
    isCommenting: Boolean,
    onCommenting: () -> Unit,
    onDismiss: () -> Unit,
    onSubmitted: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val userRoot = socialViewModel.getUser(commentRoot?.userId.toString())
    var commentText by remember { mutableStateOf("") }
    var isReply by remember(commentRoot) { mutableStateOf(commentRoot != null) }
    LaunchedEffect(isCommenting, isReply) {
        if (isCommenting || isReply) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus(force = true)
        }
    }
    // Dua input ve trang thai ban dau sau khi gui
    fun resetInputState() {
        commentText = ""
        isReply = false
    }

    // Submit comment va reset input
    fun submitComment() {
        if (commentText.isBlank()) return
        val text = commentText.trim()
        socialViewModel.onAction(
            SocialAction.AddComment(
                postId = postId,
                commentText = text,
                userId = currentUser?.id.toString(),
                parentId = if (isReply) commentRoot?.id else null,
            )
        )
        resetInputState()
        focusManager.clearFocus(force = true)
        onDismiss()
        onSubmitted()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isCommenting) {
            EmotionRow(
                onSelect = {
                    commentText += it
                    onCommenting()
                }
            )
        }
        if (isReply) {
            ReplyInfoRow(
                userName = userRoot.userName,
                onCancel = {
                    isReply = false
                    focusManager.clearFocus(force = true)
                    onDismiss()
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = if (isCommenting) Alignment.Top else Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Avatar(avatarUrl = currentUser?.avatarUrl, avatarSize = 40)

            CommentTextEditor(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
                commentText = commentText,
                isCommenting = isCommenting,
                focusRequester = focusRequester,
                onValueChange = {
                    commentText = it
                    onCommenting()
                },
            )
        }

        if (commentText.isNotEmpty() || isCommenting) {
            SubmitCommentRow(
                canSubmit = commentText.isNotEmpty(),
                onSubmit = { submitComment() }
            )
        }
    }
}

@Composable
private fun ReplyInfoRow(
    userName: String,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(50.dp))
        Text(
            text = "Trả lời $userName ",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = ".Hủy",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onCancel)
        )
    }
}

@Composable
private fun CommentTextEditor(
    modifier: Modifier,
    commentText: String,
    isCommenting: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = commentText,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            decorationBox = { innerTextField ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                }
            },
            minLines = 1,
            maxLines = 5,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .heightIn(if (isCommenting) 90.dp else 45.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.LightGray.copy(0.5f))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun SubmitCommentRow(
    canSubmit: Boolean,
    onSubmit: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canSubmit) TikTokRed else TikTokRed.copy(alpha = 0.6f),
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(34.dp)
                .width(52.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
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
