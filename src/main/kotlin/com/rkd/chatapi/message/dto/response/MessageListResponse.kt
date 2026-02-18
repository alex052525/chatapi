package com.rkd.chatapi.message.dto.response

data class MessageListResponse(
    val messages: List<MessageInfoResponse>,
    val nextCursor: Long?,
    val hasNext: Boolean
)
