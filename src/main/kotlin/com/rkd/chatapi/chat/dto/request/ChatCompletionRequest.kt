package com.rkd.chatapi.chat.dto.request

data class ChatCompletionRequest(
    val conversationId: Long,
    val content: String
)