package com.rkd.chatapi.chat.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiStreamChatCompletionResponse(
    val choices: List<OpenAiStreamChatChoice>
)
