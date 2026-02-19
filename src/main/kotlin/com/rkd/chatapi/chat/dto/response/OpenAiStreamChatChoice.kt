package com.rkd.chatapi.chat.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiStreamChatChoice(
    val delta: OpenAiChatDelta,
    @JsonProperty("finish_reason")
    val finishReason: String? = null
)
