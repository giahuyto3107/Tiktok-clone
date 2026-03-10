package com.example.tiktok_clone.features.inbox.ui.chatState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.fakeData.FakeMessData
import com.example.tiktok_clone.features.social.model.Message
import com.example.tiktok_clone.ui.theme.GrayBackground

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message> = FakeMessData.messages,
    currentUser: String = FakeMessData.ME,
){
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }
    val showLastMessage = remember(messages) {
        messages
            .mapIndexedNotNull { index, message ->
                val nextMessage = messages.getOrNull(index + 1) //index - 1 chỉ lấy tin nhắn đầu
                // kiểm tra xem có phải là tin nhắn cuối cùng của me hoặc other ko
                val isLastInGroup = nextMessage == null || nextMessage.senderId != message.senderId
                if (isLastInGroup) message.id else null
            }
            .toSet()
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(GrayBackground)
            .then(modifier),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ){
        items(
            items = messages,
            key = {it.id}){
            message ->
            val index = messages.indexOf(message)
            val prev = messages.getOrNull(index - 1)
            if(prev?.senderId != message.senderId && index != 0) // tạo dòng để phân biệt tin nhắn user và other
                Spacer(modifier = Modifier.height(16.dp))
            Messageline(
                message = message,
                isCurrentUser = message.senderId == currentUser,
                otherUserId = message.senderId,
                isLastMessage = message.id in showLastMessage
            )
            //line
        }
    }

}