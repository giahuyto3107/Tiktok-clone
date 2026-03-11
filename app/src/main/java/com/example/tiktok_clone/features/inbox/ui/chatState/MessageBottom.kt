package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.ui.components.EmotionRow
import com.example.tiktok_clone.ui.theme.BlueAccent
import com.example.tiktok_clone.ui.theme.GrayBackground

@Composable
fun MessageBottom() {
    Column(
        modifier = Modifier
            .background(GrayBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { println("Selected URI: $it") }
        }
        var messageText by remember { mutableStateOf("") }
        EmotionRow(onSelect = { messageText += it })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(color = Color.White)
                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (messageText.isEmpty()) BlueAccent else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "State",
                    modifier = Modifier
                        .size(34.dp),
                    tint = Color.White
                )
            }
            TextField(
                value = messageText,
                onValueChange = {
                    messageText = it
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                maxLines = 5,
                placeholder = {
                    Text(
                        "Nhắn tin...", color = Color.Gray,
                        fontSize = 20.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                ),
                modifier = Modifier
                    .weight(1f),
            )
            if (messageText.isEmpty())
                MessageBottomItems(
                    onGalleryClick = {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia
                                    .ImageAndVideo
                            )
                        )
                    }
                )
            else
                MessageBottomInputSend()
        }
    }
}
