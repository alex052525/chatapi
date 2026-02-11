package com.rkd.chatapi.chat.dto.response

data class OpenAiChatCompletionResponse(
    val choices: List<OpenAiChatChoice>
)
