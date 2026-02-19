package com.rkd.chatapi.conversation.domain

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.conversation.exception.ConversationNotExistException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class ConversationReader(
    private val conversationRepository: ConversationRepository
) {
    fun findConversationById(conversationId: Long): Conversation {
        return conversationRepository.findById(conversationId)
            .orElseThrow { ConversationNotExistException() }
    }

    fun findConversationsByUserId(userId: Long, pageable: Pageable): List<Conversation> {
        return conversationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
    }

    fun findConversationsByUserIdWithCursor(userId: Long, cursor: Long, pageable: Pageable): List<Conversation> {
        return conversationRepository.findByUserAndCursorOrderByCreatedAtDesc(userId, cursor, pageable)
    }
}
