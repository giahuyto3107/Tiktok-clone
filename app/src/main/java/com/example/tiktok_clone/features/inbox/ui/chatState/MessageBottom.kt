package com.example.tiktok_clone.features.inbox.ui.chatState
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.ui.theme.GrayBackground
import java.io.File
import java.io.FileOutputStream
import org.koin.androidx.compose.koinViewModel
@Composable
fun MessageBottom(
    otherUid: String,
    inboxViewModel: InboxViewModel = koinViewModel(),
    onSend: (String) -> Unit = {},
) {
    var messageText by remember { mutableStateOf("") }
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
            inboxViewModel.sendMessageWithFile(
                otherUid = otherUid,
                file = file,
                type = type,
                content = messageText.ifBlank { null },
            )
        }
    }
    Column(
        modifier = Modifier
            .background(GrayBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EmotionRow(onSelect = { messageText += it })
        Box(
            modifier = Modifier.fillMaxWidth(),
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
            MessageBottomItems(
                isMessage = messageText.isNotEmpty(),
                onGalleryClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageAndVideo
                        )
                    )
                },
                onSendClick = {
                    val text = messageText.trim()
                    if (text.isNotEmpty() && otherUid.isNotEmpty()) {
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
        FileOutputStream(file).use { out ->
            input.use { inp ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytes = inp.read(buffer)
                while (bytes >= 0) {
                    out.write(buffer, 0, bytes)
                    bytes = inp.read(buffer)
                }
            }
        }
        file
    } catch (_: Exception) {
        null
    }
}