package com.rkd.chatapi.conversation.dto.response

data class ConversationListResponse(
    val conversations: List<ConversationInfoResponse>,
    val nextCursor: Long?,
    val hasNext: Boolean
)
