package com.rkd.chatapi.chat.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiChatDelta(
    val role: String? = null,
    val content: String? = null
)
