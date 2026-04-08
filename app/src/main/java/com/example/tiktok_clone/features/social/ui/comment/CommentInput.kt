package com.example.tiktok_clone.features.social.ui.comment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.components.resolvePickedCommentImage
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowUp
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import com.example.tiktok_clone.ui.theme.TikTokRed
import java.io.File
import coil.compose.AsyncImage
import com.example.tiktok_clone.features.social.ui.components.EmotionRow

@Composable
fun CommentInput(
    socialViewModel: SocialViewModel = koinViewModel(),
    postId: String,
    currentUser: User?,
    commentRoot: Comment?,
    isCommenting: Boolean,
    onCommenting: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val userRoot = socialViewModel.getUser(commentRoot?.userId.toString())
    var commentText by remember { mutableStateOf("") }
    var isReply by remember(commentRoot) { mutableStateOf(commentRoot != null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    LaunchedEffect(isCommenting, isReply) {
        if (isCommenting || isReply) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus(force = true)
        }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val pickedImage =
            resolvePickedCommentImage(context, uri) ?: return@rememberLauncherForActivityResult
        selectedImageUri = pickedImage.uri
        selectedImageFile = pickedImage.file
        onCommenting()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.width(50.dp))
                Text(
                    text = "Trả lời ${userRoot.userName} ",
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
                    modifier = Modifier.clickable {
                        isReply = false
                        focusManager.clearFocus(force = true)
                        onDismiss()
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = if (isCommenting) Alignment.Top else Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Avatar(avatarUrl = currentUser?.avatarUrl, avatarSize = 40)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = {
                        commentText = it
                        onCommenting()
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    decorationBox = { innerTextField ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (selectedImageUri != null) {
                                Box(modifier = Modifier.size(50.dp)) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Selected image",
                                        modifier = Modifier
                                            .matchParentSize()
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(18.dp)
                                            .clip(RoundedCornerShape(9.dp))
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .clickable {
                                                selectedImageUri = null
                                                selectedImageFile = null
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Remove",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
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
                        .heightIn(if (isCommenting || selectedImageFile != null) 90.dp else 45.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.LightGray.copy(0.5f))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )

                if (!isCommenting) {
                    CommentInputItem(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                        onGalleryClick = {
                            launcher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }
            }
        }

        if (commentText.isNotEmpty() || isCommenting || selectedImageFile != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                CommentInputItem(
                    onGalleryClick = {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (commentText.isBlank() && selectedImageFile == null) return@Button
                        val text = commentText.trim()
                        val selectedFile = selectedImageFile
                        if (selectedFile != null) {
                            socialViewModel.onAction(
                                SocialAction.AddCommentWithImage(
                                    postId = postId,
                                    commentText = text,
                                    parentId = if(isReply) commentRoot?.id else null,
                                    file = selectedFile,
                                )
                            )
                        } else {
                            socialViewModel.onAction(
                                SocialAction.AddComment(
                                    postId = postId,
                                    commentText = text,
                                    userId = currentUser?.id.toString(),
                                    parentId = if(isReply) commentRoot?.id else null,
                                )
                            )
                        }
                        commentText = ""
                        selectedImageUri = null
                        selectedImageFile = null
                        isReply = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (commentText.isEmpty() && selectedImageFile == null)
                            TikTokRed.copy(alpha = 0.6f) else TikTokRed,
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
    }
}
