package com.rkd.chatapi.chat.dto.request

import com.rkd.chatapi.chat.dto.OpenAiChatMessage

data class OpenAiChatCompletionRequest(
    val model: String,
    val messages: List<OpenAiChatMessage>
)
