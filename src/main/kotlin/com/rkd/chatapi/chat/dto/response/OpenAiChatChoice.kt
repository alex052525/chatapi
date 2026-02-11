package com.rkd.chatapi.chat.dto.response

import com.rkd.chatapi.chat.dto.OpenAiChatMessage

data class OpenAiChatChoice(
    val message: OpenAiChatMessage
)
