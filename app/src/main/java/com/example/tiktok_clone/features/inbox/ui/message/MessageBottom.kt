package com.example.tiktok_clone.features.inbox.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.social.ui.components.EmotionRow

@Composable
// Bottom bar nhap tin nhan
fun MessageBottom(
    otherUid: String,
    onSend: (String) -> Unit = {},
) {
    var messageText by remember { mutableStateOf("") }

    // Gui tin nhan text
    fun sendMessage() {
        val text = messageText.trim()
        if (text.isNotEmpty() && otherUid.isNotEmpty()) {
            onSend(text)
            messageText = ""
        }
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
                onValueChange = {
                    messageText = it
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                ),
                decorationBox = { innerTextField ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    .background(Color.LightGray.copy(0.2f))
                    .padding(horizontal = 50.dp, vertical = 10.dp)
            )
            MessageBottomInput(
                isMessage = messageText.isNotEmpty(),
                onSendClick = { sendMessage() },
            )
        }
    }
}
