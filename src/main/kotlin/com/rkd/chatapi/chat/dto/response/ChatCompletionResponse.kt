package com.rkd.chatapi.chat.dto.response

data class ChatCompletionResponse(
    val messageId: Long,
    val answer: String
)