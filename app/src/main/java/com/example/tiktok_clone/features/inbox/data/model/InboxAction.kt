package com.example.tiktok_clone.features.inbox.data.model

import java.io.File

sealed interface InboxAction {
    data class LoadChats(
        val limit: Int? = null,
        val offset: Int? = 0,
        val force: Boolean = false,
    ) : InboxAction

    data class LoadMessages(
        val chatWithId: String,
        val force: Boolean = false,
    ) : InboxAction

    data class LoadMoreMessages(
        val chatWithId: String,
    ) : InboxAction

    data object DisconnectChat : InboxAction

    data class SendTextMessage(
        val otherUid: String,
        val content: String,
    ) : InboxAction

    data class SendMediaMessage(
        val otherUid: String,
        val imageUri: String,
        val type: String,
        val content: String? = null,
    ) : InboxAction

    data class SendMessageWithFile(
        val otherUid: String,
        val file: File,
        val type: String,
        val content: String? = null,
        val mimeType: String? = null,
    ) : InboxAction

    data object ClearError : InboxAction
}

