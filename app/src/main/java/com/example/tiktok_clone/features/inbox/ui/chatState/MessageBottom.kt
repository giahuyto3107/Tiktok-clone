package com.example.tiktok_clone.features.inbox.ui.chatState
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.ui.theme.GrayBackground
import java.io.File
import org.koin.androidx.compose.koinViewModel
@Composable
fun MessageBottom(
    otherUid: String,
    inboxViewModel: InboxViewModel = koinViewModel(),
    onSend: (String) -> Unit = {},
) {
    var messageText by remember { mutableStateOf("") }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedMimeType by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && otherUid.isNotEmpty()) {
            val mime = context.contentResolver.getType(uri)
            val type = if (mime?.startsWith("video") == true) "VIDEO" else "IMAGE"
            val ext = when {
                mime?.startsWith("video") == true -> ".mp4"
                mime == "image/jpeg" || mime == "image/jpg" -> ".jpg"
                mime == "image/png" -> ".png"
                mime == "image/gif" -> ".gif"
                mime == "image/webp" -> ".webp"
                else -> ".jpg"
            }
            val file = uriToTempFile(context, uri, ext) ?: return@rememberLauncherForActivityResult
            selectedMediaUri = uri
            selectedFile = file
            selectedType = type
            selectedMimeType = mime
        }
    }
    Column(
        modifier = Modifier
            .background(GrayBackground),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EmotionRow(onSelect = { messageText += it })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = { messageText = it },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                ),
                decorationBox = { innerTextField ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (selectedMediaUri != null) {
                            Box(modifier = Modifier.size(50.dp)) {
                                AsyncImage(
                                    model = selectedMediaUri,
                                    contentDescription = "Selected media",
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
                                            selectedMediaUri = null
                                            selectedFile = null
                                            selectedType = null
                                            selectedMimeType = null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Remove selected media",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                        Row {
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (messageText.isEmpty()) {
                                    Text(
                                        text = "Nhắn tin...",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                },
                maxLines = 5,
                minLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .padding(horizontal = 50.dp, vertical = 10.dp)
            )
            MessageBottomInput(
                isMessage = messageText.isNotEmpty() || selectedFile != null,
                onGalleryClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageAndVideo
                        )
                    )
                },
                onSendClick = {
                    val text = messageText.trim()
                    val file = selectedFile
                    if (file != null && otherUid.isNotEmpty()) {
                        inboxViewModel.sendMessageWithFile(
                            otherUid = otherUid,
                            file = file,
                            type = selectedType ?: "IMAGE",
                            content = text.ifBlank { null },
                            mimeType = selectedMimeType,
                        )
                        messageText = ""
                        selectedMediaUri = null
                        selectedFile = null
                        selectedType = null
                        selectedMimeType = null
                    } else if (text.isNotEmpty() && otherUid.isNotEmpty()) {
                        onSend(text)
                        messageText = ""
                    }
                },
            )
        }
    }
}
private fun uriToTempFile(context: Context, uri: Uri, extension: String): File? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val file = File.createTempFile("inbox_msg_", extension, context.cacheDir)
        file.outputStream().use { out -> input.copyTo(out) }
        file
    } catch (_: Exception) {
        null
    }
}