package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.conversation.dto.response.ConversationInfoResponse
import com.rkd.chatapi.conversation.dto.response.ConversationListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ConversationInfoService(
    private val conversationRepository: ConversationRepository
) {
    fun getConversations(userId: Long, cursor: Long?, size: Int): ConversationListResponse {
        val conversations = fetchConversations(userId, cursor, size)
        val hasNext = conversations.size > size
        val result = conversations.take(size)

        return ConversationListResponse(
            conversations = result.map { it.toInfoResponse() },
            nextCursor = if (hasNext) result.last().id else null,
            hasNext = hasNext
        )
    }

    private fun fetchConversations(userId: Long, cursor: Long?, size: Int): List<Conversation> {
        val pageable = PageRequest.of(0, size + 1)
        return if (cursor == null) {
            conversationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
        } else {
            conversationRepository.findByUserAndCursorOrderByCreatedAtDesc(userId, cursor, pageable)
        }
    }

    private fun Conversation.toInfoResponse() = ConversationInfoResponse(title = title)
}
