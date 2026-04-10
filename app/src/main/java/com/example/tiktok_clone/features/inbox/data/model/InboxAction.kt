package com.example.tiktok_clone.features.inbox.data.model

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

    data object DisconnectChat : InboxAction

    data class SendTextMessage(
        val otherUid: String,
        val content: String,
    ) : InboxAction

    data object ClearError : InboxAction
}

